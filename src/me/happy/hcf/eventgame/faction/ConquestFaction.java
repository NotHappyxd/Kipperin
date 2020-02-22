package me.happy.hcf.eventgame.faction;

import com.doctordark.util.BukkitUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.happy.hcf.eventgame.CaptureZone;
import me.happy.hcf.eventgame.EventType;
import me.happy.hcf.faction.claim.Claim;
import me.happy.hcf.faction.type.ClaimableFaction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

/**
 * Represents a 'Conquest' faction.
 */
public class ConquestFaction extends CapturableFaction implements ConfigurationSerializable {

    private final EnumMap<ConquestZone, CaptureZone> captureZones = new EnumMap<>(ConquestZone.class);

    public ConquestFaction(String name) {
        super(name);
    }

    public ConquestFaction(Map<String, Object> map) {
        super(map);

        Object object;
        if ((object = map.get("red")) instanceof CaptureZone) {
            captureZones.put(ConquestZone.RED, (CaptureZone) object);
        }

        if ((object = map.get("green")) instanceof CaptureZone) {
            captureZones.put(ConquestZone.GREEN, (CaptureZone) object);
        }

        if ((object = map.get("blue")) instanceof CaptureZone) {
            captureZones.put(ConquestZone.BLUE, (CaptureZone) object);
        }

        if ((object = map.get("yellow")) instanceof CaptureZone) {
            captureZones.put(ConquestZone.YELLOW, (CaptureZone) object);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        for (Map.Entry<ConquestZone, CaptureZone> entry : captureZones.entrySet()) {
            map.put(entry.getKey().name().toLowerCase(), entry.getValue());
        }

        return map;
    }

    @Override
    public EventType getEventType() {
        return EventType.CONQUEST;
    }

    @Override
    public void printDetails(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(getDisplayName(sender));

        for (Claim claim : claims) {
            Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.RED +
                    '(' + ClaimableFaction.ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ() + ')');
        }

        sender.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public void setZone(ConquestZone conquestZone, CaptureZone captureZone) {
        switch (conquestZone) {
            case RED:
                captureZones.put(ConquestZone.RED, captureZone);
                break;
            case BLUE:
                captureZones.put(ConquestZone.BLUE, captureZone);
                break;
            case GREEN:
                captureZones.put(ConquestZone.GREEN, captureZone);
                break;
            case YELLOW:
                captureZones.put(ConquestZone.YELLOW, captureZone);
                break;
            default:
                throw new AssertionError("Unsupported operation");
        }
    }

    public CaptureZone getRed() {
        return captureZones.get(ConquestZone.RED);
    }

    public CaptureZone getGreen() {
        return captureZones.get(ConquestZone.GREEN);
    }

    public CaptureZone getBlue() {
        return captureZones.get(ConquestZone.BLUE);
    }

    public CaptureZone getYellow() {
        return captureZones.get(ConquestZone.YELLOW);
    }

    public Collection<ConquestZone> getConquestZones() {
        return ImmutableSet.copyOf(captureZones.keySet());
    }

    @Override
    public List<CaptureZone> getCaptureZones() {
        return ImmutableList.copyOf(captureZones.values());
    }

    public enum ConquestZone {

        RED(ChatColor.RED, "Red"),
        BLUE(ChatColor.AQUA, "Blue"),
        YELLOW(ChatColor.YELLOW, "Yellow"),
        GREEN(ChatColor.GREEN, "Green");

        private static final Map<String, ConquestZone> BY_NAME;

        static {
            ImmutableMap.Builder<String, ConquestZone> builder = ImmutableMap.builder();
            for (ConquestZone zone : values()) {
                builder.put(zone.name().toUpperCase(), zone);
            }

            BY_NAME = builder.build();
        }

        private final String name;
        private final ChatColor color;

        ConquestZone(ChatColor color, String name) {
            this.color = color;
            this.name = name;
        }

        public static ConquestZone getByName(String name) {
            return BY_NAME.get(name.toUpperCase());
        }

        public static Collection<String> getNames() {
            return new ArrayList<>(BY_NAME.keySet());
        }

        public ChatColor getColor() {
            return color;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return color.toString() + name;
        }
    }
}
