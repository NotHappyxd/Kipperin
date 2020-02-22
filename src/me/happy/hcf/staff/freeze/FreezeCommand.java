package me.happy.hcf.staff.freeze;

import lombok.val;
import me.happy.hcf.HCF;
import me.happy.hcf.files.MessageFile;
import me.happy.hcf.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage. /freeze <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online!");
            return true;
        }


        if (!sender.hasPermission("hcf.command.freeze.*") && target.hasPermission("hcf.command.freeze.bypass")) {
            sender.sendMessage(ChatColor.RED + "You cannot freeze this player!");
            return true;
        }

        val state = HCF.getPlugin().getFreezeManager().getFrozen().get(target.getUniqueId());
        val freezeManager = HCF.getPlugin().getFreezeManager();

        if (!freezeManager.getFrozen().containsKey(target.getUniqueId())) {
            freezeManager.setState(target, FreezeState.GUI);
            sender.sendMessage(CC.translate(MessageFile.getConfig().getString("freeze.staff.inventory-lock").replace("%player%", target.getName())));
            target.sendMessage(CC.translate(MessageFile.getConfig().getString("freeze.other.inventory-lock")));
            return true;
        }
        switch (state) {
            case GUI:
                freezeManager.setState(target, FreezeState.NO_GUI);
                sender.sendMessage(CC.translate(MessageFile.getConfig().getString("freeze.staff.remove-inv-lock").replace("%player%", target.getName())));
                target.sendMessage(CC.translate(MessageFile.getConfig().getString("freeze.other.remove-inv-lock")));
                break;
            case NO_GUI:
                freezeManager.setState(target, FreezeState.NONE);
                sender.sendMessage(CC.translate(MessageFile.getConfig().getString("freeze.staff.unfreeze").replace("%player%", target.getName())));
                target.sendMessage(CC.translate(MessageFile.getConfig().getString("freeze.other.unfreeze")));
                break;
        }

        return true;
    }
}
