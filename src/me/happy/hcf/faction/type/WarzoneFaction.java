package me.happy.hcf.faction.type;

import me.happy.hcf.HCF;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Represents the {@link WarzoneFaction}.
 */
public class WarzoneFaction extends Faction {

    public WarzoneFaction() {
        super("Warzone");
    }

    public WarzoneFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return HCF.getPlugin().getConfiguration().getRelationColourWarzone() + getName();
    }
}
