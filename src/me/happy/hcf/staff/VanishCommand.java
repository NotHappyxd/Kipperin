package me.happy.hcf.staff;

import me.happy.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (!HCF.getPlugin().getStaffModeManager().getVanished().contains(player.getUniqueId())) {
            Bukkit.getServer().getOnlinePlayers().forEach(online -> online.hidePlayer(player));
            player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.GREEN + "enabled " + ChatColor.YELLOW + "your vanish!");
            return true;
        } else {
            Bukkit.getServer().getOnlinePlayers().forEach(online -> online.showPlayer(player));
            player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.RED + "disabled " + ChatColor.YELLOW + "your vanish!");
            return true;
        }
    }
}
