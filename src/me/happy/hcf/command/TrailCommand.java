package me.happy.hcf.command;

import me.happy.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrailCommand implements CommandExecutor, TabCompleter {

    public static final Effect getEffectByName(String name) {
        for (Effect effect : Effect.values()) {
            if (effect.getName() != null && !effect.getName().equals("")) {
                if (effect.getName().toUpperCase().equals(name.toUpperCase())) {
                    return effect;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments. /trail <trail>");
            return true;
        }

        String trail = args[0];

        for (Effect effect : Effect.values()) {
            if (effect.getName() != null && !effect.getName().equals("")) {
                System.out.println(effect.getName());
            }
        }
        Effect effect = getEffectByName(args[0].toUpperCase());
        if (effect == null) {
            sender.sendMessage(ChatColor.RED + trail + " is an invalid trail. Please tab to see a list of trails.");
            return true;
        }

        Player player = (Player) sender;

        HCF.getPlugin().getUserManager().getUser(player.getUniqueId()).setTrail(effect);

        sender.sendMessage(ChatColor.GREEN + "You have set your trail to " + trail.toUpperCase() + "!");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            List<String> toReturn = new ArrayList<>();

            if (!args[0].equals("")) {
                for (Effect s : Effect.values()) {
                    //if (!s.getName().equals(""))
                    if (s.getName() != null && !s.getName().equals("")) {
                        if (s.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                            toReturn.add(s.getName().toUpperCase());
                    }
                }
            } else {
                for (Effect s : Effect.values()) {
                    //if (!s.getName().equals(""))
                    if (s.getName() != null && !s.getName().equals("")) {
                        if (s.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                            toReturn.add(s.getName().toUpperCase());
                    }
                }
            }

            Collections.sort(toReturn);

            return toReturn;
        }

        return null;
    }
}
