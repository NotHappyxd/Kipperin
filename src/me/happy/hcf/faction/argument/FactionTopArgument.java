package me.happy.hcf.faction.argument;

import com.doctordark.util.command.CommandArgument;
import me.happy.hcf.HCF;
import me.happy.hcf.faction.type.PlayerFaction;
import me.happy.hcf.files.MessageFile;
import me.happy.hcf.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FactionTopArgument extends CommandArgument {

    public FactionTopArgument() {
        super("top", "Shows the list of top factions");
    }

    @Override
    public String getUsage(String label) {
        return "/f top <points;balance;>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length != 2) {
            sender.sendMessage(ChatColor.RED + getUsage(label));
            return true;
        }

        if (!args[1].equalsIgnoreCase("points") && !args[1].equalsIgnoreCase("balance")) {
            sender.sendMessage(ChatColor.RED + getUsage(label));
            return true;
        }

        List<PlayerFaction> factions = new ArrayList<>();
        if (args[1].equalsIgnoreCase("points")) {
           factions = HCF.getPlugin().getFactionManager().getFactions().stream().filter(faction -> faction instanceof PlayerFaction).map(faction -> (PlayerFaction) faction).sorted(Comparator.comparingInt(PlayerFaction::getPoints).reversed()).collect(Collectors.toList());
        }else if (args[1].equalsIgnoreCase("balance")) {
            factions = HCF.getPlugin().getFactionManager().getFactions().stream().filter(faction -> faction instanceof PlayerFaction).map(faction -> (PlayerFaction) faction).sorted(Comparator.comparingInt(PlayerFaction::getBalance).reversed()).collect(Collectors.toList());
        }

        for (String s : MessageFile.getConfig().getStringList("faction-top.prefix")) {
            sender.sendMessage(CC.translate(s));
        }

        if (factions.size() < 10) {
            for (int i = 0; i < factions.size(); i++) {
                sender.sendMessage(CC.translate(MessageFile.getConfig().getString("faction-top.position").replace("%position%", String.valueOf(i + 1)).replace("%faction-name%",
                        factions.get(i).getDisplayName(sender)).replace("%points%", String.valueOf(factions.get(i).getPoints()))));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                sender.sendMessage(CC.translate(MessageFile.getConfig().getString("faction-top.position").replace("%position%", String.valueOf(i + 1)).replace("%faction-name%",
                        factions.get(i).getDisplayName(sender)).replace("%points%", String.valueOf(factions.get(i).getPoints()))));
            }
        }

        for (String s : MessageFile.getConfig().getStringList("faction-top.suffix")) {
            sender.sendMessage(CC.translate(s));
        }
        return true;
    }
}
