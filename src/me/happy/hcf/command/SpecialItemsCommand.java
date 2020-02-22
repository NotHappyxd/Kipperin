package me.happy.hcf.command;

import com.doctordark.util.ItemBuilder;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialItemsCommand implements CommandExecutor, TabCompleter {

    private List<String> names = new ArrayList<>(Arrays.asList("SWAPPER", "ANTI-BUILD", "ROTTEN", "GRAPPLING_HOOK"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Invalid usage! /specialitem <player> <item> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online!");
            return true;
        }

        Integer amount = Ints.tryParse(args[2]);

        if (amount == null) {
            sender.sendMessage(ChatColor.RED + args[2] + " is not a valid number.");
            return true;
        }

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + args[2] + " is less than or equal to zero!");
            return true;
        }

        if (names.contains(args[1].toUpperCase())) {
            switch (args[1].toUpperCase()) {
                case "SWAPPER":
                    ItemStack SWAPPER = new ItemBuilder(Material.SNOW_BALL, amount).displayName(ChatColor.AQUA + "Snowball Swapper").lore(ChatColor.GRAY + "If this hits someone, you swap locations", ChatColor.GRAY + "Note it has a 8 block limit.").build();
                    target.getInventory().addItem(SWAPPER);
                    break;
                case "ANTI-BUILD":
                    ItemStack ANTI_BUILD = new ItemBuilder(Material.EGG, amount).displayName(ChatColor.AQUA + "Anti-Build Swapper").lore(ChatColor.GRAY + "If this hits someone, they cannot build for 10 seconds", "Note it has a 8 block limit.").build();
                    target.getInventory().addItem(ANTI_BUILD);
                    break;
                case "ROTTEN":
                    ItemStack ROTTEN = new ItemBuilder(Material.EGG, amount).displayName(ChatColor.AQUA + "Rotten Egg").lore(ChatColor.GRAY + "If this hits someone, they will get a random effect!", "Note it has a 8 block limit.").build();
                    target.getInventory().addItem(ROTTEN);
                    break;
                case "GRAPPLING_HOOK":
                    ItemStack GRAPPLING_HOOK = new ItemBuilder(Material.FISHING_ROD, amount).displayName(ChatColor.GOLD + "Grappling Hook").lore(ChatColor.YELLOW + "Propel yourself!").build();
                    target.getInventory().addItem(GRAPPLING_HOOK);
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> toReturn;
        if (args.length == 1) {

            if (args[0].equals("")) {
                toReturn = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            } else {
                toReturn = Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().startsWith(args[0])).map(Player::getName).collect(Collectors.toList());
            }
            return toReturn;
        }

        if (args.length == 2) {

            if (args[1].equals("")) {
                toReturn = names;
            }else {
                toReturn = names.stream().filter(string -> string.startsWith(args[1])).collect(Collectors.toList());
            }

            return toReturn;
        }

        return Collections.emptyList();
    }
}
