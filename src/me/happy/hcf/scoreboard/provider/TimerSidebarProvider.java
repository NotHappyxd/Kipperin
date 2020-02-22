package me.happy.hcf.scoreboard.provider;

import com.google.common.collect.Ordering;
import me.happy.hcf.ConfigurationService;
import me.happy.hcf.DateTimeFormats;
import me.happy.hcf.HCF;
import me.happy.hcf.eventgame.EventTimer;
import me.happy.hcf.eventgame.eotw.EotwHandler;
import me.happy.hcf.eventgame.faction.ConquestFaction;
import me.happy.hcf.eventgame.faction.EventFaction;
import me.happy.hcf.eventgame.faction.KothFaction;
import me.happy.hcf.eventgame.tracker.ConquestTracker;
import me.happy.hcf.faction.type.PlayerFaction;
import me.happy.hcf.pvpclass.PvpClass;
import me.happy.hcf.pvpclass.archer.ArcherClass;
import me.happy.hcf.pvpclass.archer.ArcherMark;
import me.happy.hcf.pvpclass.bard.BardClass;
import me.happy.hcf.scoreboard.SidebarEntry;
import me.happy.hcf.scoreboard.SidebarProvider;
import me.happy.hcf.sotw.SotwTimer;
import me.happy.hcf.timer.PlayerTimer;
import me.happy.hcf.timer.Timer;
import me.happy.hcf.util.DurationFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class TimerSidebarProvider implements SidebarProvider {

    public static final ThreadLocal<DecimalFormat> CONQUEST_FORMATTER = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("00.0");
        }
    };

    private static final Comparator<Map.Entry<UUID, ArcherMark>> ARCHER_MARK_COMPARATOR = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

    private final HCF plugin;

    public TimerSidebarProvider(HCF plugin) {
        this.plugin = plugin;
    }

    private static String handleBardFormat(long millis, boolean trailingZero) {
        return (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(millis * 0.001);
    }

    @Override
    public List<SidebarEntry> getLines(Player player) {
        List<SidebarEntry> lines = new ArrayList<>();

        if (ConfigurationService.KITMAP) {
            lines.add(new SidebarEntry(ChatColor.RED + "Kills" + ChatColor.GRAY + ":" + ChatColor.RESET, " ", player.getStatistic(Statistic.PLAYER_KILLS)));
            lines.add(new SidebarEntry(ChatColor.RED + "Deaths" + ChatColor.GRAY + ": " + ChatColor.RESET, "", player.getStatistic(Statistic.DEATHS)));
            lines.add(message("fadsfhakjdfhalkjdfhalkjdfhaldskjfhalskjd"));
            // lines.add(new SidebarEntry("fadsfhakjdfhalkj", "dfhalkjdfhaldskj", "fhalskjd"));
        }


        EotwHandler.EotwRunnable eotwRunnable = plugin.getEotwHandler().getRunnable();
        if (eotwRunnable != null) {
            long remaining = eotwRunnable.getMillisUntilStarting();
            if (remaining > 0L) {
                lines.add(new SidebarEntry(ChatColor.RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.RED + " starts", " in " + ChatColor.BOLD +
                        DurationFormatter.getRemaining(remaining, true)));
            } else if ((remaining = eotwRunnable.getMillisUntilCappable()) > 0L) {
                lines.add(new SidebarEntry(ChatColor.RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.RED + " cappable", " in " + ChatColor.BOLD +
                        DurationFormatter.getRemaining(remaining, true)));
            }
        }

        SotwTimer.SotwRunnable sotwRunnable = plugin.getSotwTimer().getSotwRunnable();
        if (sotwRunnable != null) {
            if (plugin.getSotwTimer().getEnabled().contains(player.getUniqueId())) {
                lines.add(new SidebarEntry(ChatColor.STRIKETHROUGH.toString() + ChatColor.DARK_GREEN.toString() + ChatColor.BOLD, "SOTW", ChatColor.GRAY + ": " + ChatColor.GOLD +
                        DurationFormatter.getRemaining(sotwRunnable.getRemaining(), true)));
            } else {
                lines.add(new SidebarEntry(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD, "SOTW", ChatColor.GRAY + ": " + ChatColor.GOLD +
                        DurationFormatter.getRemaining(sotwRunnable.getRemaining(), true)));
            }
        }

        EventTimer eventTimer = plugin.getTimerManager().getEventTimer();
        List<SidebarEntry> conquestLines = null;

        EventFaction eventFaction = eventTimer.getEventFaction();
        if (eventFaction instanceof KothFaction) {
            lines.add(new SidebarEntry(eventTimer.getScoreboardPrefix(), eventFaction.getScoreboardName() + ChatColor.GRAY, ": " +
                    ChatColor.GOLD + DurationFormatter.getRemaining(eventTimer.getRemaining(), true)));
        } else if (eventFaction instanceof ConquestFaction) {
            ConquestFaction conquestFaction = (ConquestFaction) eventFaction;
            DecimalFormat format = CONQUEST_FORMATTER.get();

            conquestLines = new ArrayList<>();
            conquestLines.add(new SidebarEntry(ChatColor.BLUE.toString(), ChatColor.BOLD + conquestFaction.getName() + ChatColor.GRAY, ":"));

            conquestLines.add(new SidebarEntry("  " +
                    ChatColor.RED.toString() + conquestFaction.getRed().getScoreboardRemaining(),
                    ChatColor.RESET + " ",
                    ChatColor.YELLOW.toString() + conquestFaction.getYellow().getScoreboardRemaining()));

            conquestLines.add(new SidebarEntry("  " +
                    ChatColor.GREEN.toString() + conquestFaction.getGreen().getScoreboardRemaining(),
                    ChatColor.RESET + " " + ChatColor.RESET,
                    ChatColor.AQUA.toString() + conquestFaction.getBlue().getScoreboardRemaining()));

            // Show the top 3 factions next.
            ConquestTracker conquestTracker = (ConquestTracker) conquestFaction.getEventType().getEventTracker();
            int count = 0;
            for (Map.Entry<PlayerFaction, Integer> entry : conquestTracker.getFactionPointsMap().entrySet()) {
                String factionName = entry.getKey().getName();
                if (factionName.length() > 14) factionName = factionName.substring(0, 14);
                conquestLines.add(new SidebarEntry(ChatColor.LIGHT_PURPLE, ChatColor.BOLD + factionName, ChatColor.GRAY + ": " + ChatColor.YELLOW + entry.getValue()));
                if (++count == 3) break;
            }
        }

        // Show the current PVP Class statistics of the player.
        PvpClass pvpClass = plugin.getPvpClassManager().getEquippedClass(player);
        if (pvpClass != null) {
            lines.add(new SidebarEntry(ChatColor.YELLOW, "Active Class" + ChatColor.GRAY + ": ", ChatColor.GREEN + pvpClass.getName()));
            if (pvpClass instanceof BardClass) {
                BardClass bardClass = (BardClass) pvpClass;
                lines.add(new SidebarEntry(ChatColor.DARK_PURPLE + " \u00bb ", ChatColor.LIGHT_PURPLE + "Energy", ChatColor.GRAY + ": " + ChatColor.GOLD +
                        handleBardFormat(bardClass.getEnergyMillis(player), true)));

                long remaining = bardClass.getRemainingBuffDelay(player);
                if (remaining > 0) {
                    lines.add(new SidebarEntry(ChatColor.DARK_PURPLE + " \u00bb ", ChatColor.LIGHT_PURPLE + "Buff Delay",
                            ChatColor.GRAY + ": " + ChatColor.GOLD + DurationFormatter.getRemaining(remaining, true)));
                }
            } else if (pvpClass instanceof ArcherClass) {
                ArcherClass archerClass = (ArcherClass) pvpClass;

                List<Map.Entry<UUID, ArcherMark>> entryList = Ordering.from(ARCHER_MARK_COMPARATOR).sortedCopy(archerClass.getSentMarks(player).entrySet());
                entryList = entryList.subList(0, Math.min(entryList.size(), 3));
                for (Map.Entry<UUID, ArcherMark> entry : entryList) {
                    ArcherMark archerMark = entry.getValue();
                    Player target = Bukkit.getPlayer(entry.getKey());
                    if (target != null) {
                        ChatColor levelColour;
                        switch (archerMark.currentLevel) {
                            case 1:
                                levelColour = ChatColor.GREEN;
                                break;
                            case 2 | 3:
                                levelColour = ChatColor.RED;
                                break;
                            default:
                                levelColour = ChatColor.YELLOW;
                                break;
                        }

                        // Add the current mark level to scoreboard.
                        String targetName = target.getName();
                        targetName = targetName.substring(0, Math.min(targetName.length(), 15));
                        lines.add(new SidebarEntry(ChatColor.LIGHT_PURPLE + " \u00bb" + ChatColor.RED, ' ' + targetName,
                                ChatColor.YELLOW.toString() + levelColour + " [Mark " + archerMark.currentLevel + ']'));
                    }
                }
            }
        }

        Collection<Timer> timers = plugin.getTimerManager().getTimers();
        for (Timer timer : timers) {
            if (timer instanceof PlayerTimer) {
                PlayerTimer playerTimer = (PlayerTimer) timer;
                long remaining = playerTimer.getRemaining(player);
                if (remaining <= 0) continue;

                String timerName = playerTimer.getName();
                if (timerName.length() > 14) timerName = timerName;
                lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), timerName + ChatColor.GRAY, ": " + ChatColor.GOLD + DurationFormatter.getRemaining(remaining, true)));
            }
        }

        if (conquestLines != null && !conquestLines.isEmpty()) {
            /*
            if (!lines.isEmpty()) {
                conquestLines.add(new SidebarEntry(" ", "  ", " "));
            }

             */

            conquestLines.addAll(lines);
            lines = conquestLines;
        }

        if (!lines.isEmpty()) {
            lines.add(0, new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + "-----------", "---------"));
            lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + "----------", "----------"));
        }


        return lines;
    }

    public SidebarEntry message(String message) {
        if (message.length() <= 16) {
            return new SidebarEntry(message);
        }

        if (message.length() <= 32) {
            return new SidebarEntry(message.substring(0, 16), message.substring(16), "");
        }

        if (message.length() <= 40) {
            return new SidebarEntry(message.substring(0, 16), message.substring(16, 32), message.substring(32, 40));
        }

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + message + " is over the character limit of 48. ");
        return null;

    }
}