package me.happy.hcf.listener;

import me.happy.hcf.HCF;
import me.happy.hcf.faction.event.FactionChatEvent;
import me.happy.hcf.faction.struct.ChatChannel;
import me.happy.hcf.faction.type.Faction;
import me.happy.hcf.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.Set;

public class ChatListener implements Listener {

    private HCF plugin;

    public ChatListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);
        ChatChannel chatChannel = playerFaction == null ? ChatChannel.PUBLIC : playerFaction.getMember(player).getChatChannel();

        // Handle faction or alliance chat modes.
        Set<Player> recipients = event.getRecipients();

        if (chatChannel == ChatChannel.FACTION || chatChannel == ChatChannel.ALLIANCE) {
            if (isGlobalChannel(message)) { // allow players to use '!' to bypass friendly chat.
                message = message.substring(1).trim();
                event.setMessage(message);
            } else {
                Collection<Player> online = playerFaction.getOnlinePlayers();
                if (chatChannel == ChatChannel.ALLIANCE) {
                    Collection<PlayerFaction> allies = playerFaction.getAlliedFactions();
                    for (PlayerFaction ally : allies) {
                        online.addAll(ally.getOnlinePlayers());
                    }
                }

                recipients.retainAll(online);
                event.setFormat(chatChannel.getRawFormat(player));

                Bukkit.getPluginManager().callEvent(new FactionChatEvent(true, playerFaction, player, chatChannel, recipients, event.getMessage()));
                return;
            }
        }

        // Handle the custom messaging here.
        event.setCancelled(true);

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(this.getFormattedMessage(player, playerFaction, message, console));

        for (Player recipient : event.getRecipients()) {
            recipient.sendMessage(this.getFormattedMessage(player, playerFaction, message, recipient));
        }

        // HCF.getPlugin().getLogManager().formatChatMessage(message, player.getName());
    }

    private String getFormattedMessage(Player player, PlayerFaction playerFaction, String message, CommandSender viewer) {
        String tag = playerFaction == null ? ChatColor.YELLOW + Faction.FACTIONLESS_PREFIX : playerFaction.getDisplayName(viewer);
        String prefix = HCF.getPlugin().getChat().getPlayerPrefix(player);
        PlayerFaction playersFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(player);

        if (playersFaction != null) {
            return ChatColor.translateAlternateColorCodes('&', "&6[" + tag + "&6] &f" + prefix + player.getName() + "&f: ") + ChatColor.RESET + message;
        } else {
            return ChatColor.translateAlternateColorCodes('&', prefix + player.getName() + "&f: " + ChatColor.RESET) + message;
        }
    }

    /**
     * Checks if a message should be posted in {@link ChatChannel#PUBLIC}.
     *
     * @param input the message to check
     * @return true if the message should be posted in {@link ChatChannel#PUBLIC}
     */
    private boolean isGlobalChannel(String input) {
        int length = input.length();

        if (length <= 1 || !input.startsWith("!")) {
            return false;
        }

        for (int i = 1; i < length; i++) {
            char character = input.charAt(i);

            if (character == ' ') continue;

            if (character == '/') {
                return false;
            } else {
                break;
            }
        }

        return true;
    }

}