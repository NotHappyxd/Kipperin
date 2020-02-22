package me.happy.hcf.staff;

import lombok.val;
import me.happy.hcf.HCF;
import me.happy.hcf.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        val staffManager = HCF.getPlugin().getStaffModeManager();
        if (!staffManager.getStaffMode().contains(player.getUniqueId())) {
            staffManager.getArmorMap().put(player.getUniqueId(), player.getInventory().getArmorContents());
            staffManager.getInventoryMap().put(player.getUniqueId(), player.getInventory().getContents());
            staffManager.getStaffMode().add(player.getUniqueId());
            player.getInventory().clear();
            Bukkit.getServer().getOnlinePlayers().forEach(online -> {
                online.hidePlayer(player);
            });
            staffManager.giveItem(player);
            player.setGameMode(GameMode.CREATIVE);
            sender.sendMessage(ChatColor.YELLOW + "You have " + CC.GREEN + "enabled" + CC.YELLOW + " your staff mode!");
            return true;
        }else {
            player.getInventory().clear();
            player.getInventory().setArmorContents(staffManager.getArmorMap().get(player.getUniqueId()));
            staffManager.getArmorMap().remove(player.getUniqueId());
            player.getInventory().setContents(staffManager.getInventoryMap().get(player.getUniqueId()));
            staffManager.getInventoryMap().remove(player.getUniqueId());
            staffManager.getStaffMode().remove(player.getUniqueId());
            Bukkit.getServer().getOnlinePlayers().forEach(online -> {
                online.showPlayer(player);
            });
            player.setGameMode(GameMode.SURVIVAL);
            sender.sendMessage(ChatColor.YELLOW + "You have " + CC.RED + "disabled" + CC.YELLOW + " your staff mode!");
            return true;
        }
    }
}
