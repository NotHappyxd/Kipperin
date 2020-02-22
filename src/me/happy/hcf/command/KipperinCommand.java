package me.happy.hcf.command;

import me.happy.hcf.HCF;
import me.happy.hcf.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KipperinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(CC.GREEN + "This server is running Kipperin made by Happy");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("command.kipperin.reload")) {
                sender.sendMessage(CC.RED + "No Perms");
                return true;
            }

            HCF.getPlugin().reloadConfig();
            sender.sendMessage(CC.GREEN + "Successfully reloaded Kipperin config file!");
            return true;
        }

        return true;
    }
}
