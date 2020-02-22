package me.happy.hcf.faction.type;

import me.happy.hcf.HCF;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Represents the {@link WildernessFaction}.
 */
public class WildernessFaction extends Faction {

    public WildernessFaction() {
        super("Wilderness");
    }

    public WildernessFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return HCF.getPlugin().getConfiguration().getRelationColourWilderness() + getName();
    }
}
