package me.happy.hcf.faction.argument;

import com.doctordark.util.command.CommandArgument;
import me.happy.hcf.HCF;
import me.happy.hcf.faction.FactionMember;
import me.happy.hcf.faction.struct.Role;
import me.happy.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionOpenArgument extends CommandArgument {

    private final HCF plugin;

    public FactionOpenArgument(HCF plugin) {
        super("open", "Opens the faction to the public.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }

        Player player = (Player) sender;
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }

        FactionMember factionMember = playerFaction.getMember(player.getUniqueId());

        if (factionMember.getRole() != Role.LEADER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction leader to do this.");
            return true;
        }

        boolean newOpen = !playerFaction.isOpen();
        playerFaction.setOpen(newOpen);
        playerFaction.broadcast(ChatColor.YELLOW + sender.getName() + " has " + (newOpen ? ChatColor.GREEN + "opened" : ChatColor.RED + "closed") + ChatColor.YELLOW + " the faction to public.");
        return true;
    }
}
