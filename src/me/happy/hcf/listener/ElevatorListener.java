package me.happy.hcf.listener;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ElevatorListener implements Listener {

    @EventHandler
    public void onElevatorCreate(SignChangeEvent event) {
        if (StringUtils.containsIgnoreCase(event.getLine(0), "Elevator")) {
            boolean up;
            if (StringUtils.containsIgnoreCase(event.getLine(1), "Up")) {
                up = true;
            } else {
                if (!StringUtils.containsIgnoreCase(event.getLine(1), "Down")) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Invalid sign elevator type (UP, DOWN).");
                    event.setLine(0, ChatColor.BLUE + "[Elevator]");
                    event.setLine(1, ChatColor.RED + "Error");
                    return;
                }
                up = false;
            }
            event.setLine(0, ChatColor.BLUE + "[Elevator]");
            event.setLine(1, up ? "Up" : "Down");
            event.setLine(2, "");
            event.setLine(3, "");
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if (StringUtils.containsIgnoreCase(sign.getLine(0), "[Elevator]")) {
                    Location loc = block.getLocation();
                    if (sign.getLine(1).equalsIgnoreCase("Up")) {
                        while (true) {
                            Location location1 = loc.add(0.0, 1.0, 0.0);
                            Location location2 = loc.add(0.0, 2.0, 0.0);
                            if (location1.getY() >= 250.0) {
                                break;
                            }
                            if (location1.getBlock().getType() == Material.AIR && location2.getBlock().getType() == Material.AIR) {
                                player.teleport(new Location(location1.getWorld(), location1.getBlockX(), location1.getBlockY(), location1.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                                break;
                            }
                        }
                    }
                    if (sign.getLine(1).equalsIgnoreCase("Down")) {
                        while (true) {
                            Location location1 = loc.subtract(0.0, 1.0, 0.0);
                            Location location2 = loc.subtract(0.0, 2.0, 0.0);
                            if (location1.getY() <= 0.0) {
                                break;
                            }
                            if (location1.getBlock().getType() == Material.AIR && location2.getBlock().getType() == Material.AIR) {
                                player.teleport(new Location(location2.getWorld(), location2.getBlockX(), location2.getBlockY(), location2.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}