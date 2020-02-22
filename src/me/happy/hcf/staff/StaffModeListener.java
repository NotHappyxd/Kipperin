package me.happy.hcf.staff;

import com.doctordark.util.ItemBuilder;
import me.happy.hcf.HCF;
import me.happy.hcf.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class StaffModeListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (HCF.getPlugin().getStaffModeManager().getStaffMode().contains(e.getPlayer().getUniqueId()))
            HCF.getPlugin().getStaffModeManager().getStaffMode().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (HCF.getPlugin().getStaffModeManager().getStaffMode().contains(e.getPlayer().getUniqueId()))
            HCF.getPlugin().getStaffModeManager().getStaffMode().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (HCF.getPlugin().getStaffModeManager().getStaffMode().contains(e.getPlayer().getUniqueId())) {
                if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
                    if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                        List<UUID> miners = player.getWorld().getPlayers().stream().filter(online -> !HCF.getPlugin().getStaffModeManager().getStaffMode().contains(online.getUniqueId()) && online.getLocation().getY() <= 40).map(Entity::getUniqueId).collect(Collectors.toList());
                        if (miners.isEmpty()) {
                            player.sendMessage(ChatColor.RED + "No miners online");
                        }
                        Player teleportee = Bukkit.getPlayer(miners.get(ThreadLocalRandom.current().nextInt(miners.size())));
                        player.teleport(teleportee);
                        player.sendMessage(ChatColor.YELLOW + "You have teleported to " + ChatColor.RESET + teleportee.getName());
                    }
                    if (player.getItemInHand().getType() == Material.GREEN_RECORD) {
                        List<UUID> random = Bukkit.getServer().getOnlinePlayers().stream().filter(online -> !HCF.getPlugin().getStaffModeManager().getStaffMode().contains(online.getUniqueId())).map(online -> online.getUniqueId()).collect(Collectors.toList());
                        Player teleportee = Bukkit.getPlayer(random.get(ThreadLocalRandom.current().nextInt(random.size())));
                        player.teleport(teleportee);
                        player.sendMessage(ChatColor.YELLOW + "You have teleported to " + ChatColor.RESET + teleportee.getName());
                    }
                    if (player.getItemInHand().getType() == Material.INK_SACK) {
                        if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasDisplayName()) {
                            if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Vanish: " + ChatColor.GREEN + "On")) {
                                ItemStack UNVANISH = new ItemBuilder(Material.INK_SACK, 1, (byte) 8).displayName(ChatColor.YELLOW + "Vanish: " + ChatColor.RED + "Off").build();
                                player.setItemInHand(UNVANISH);
                                Bukkit.getServer().getOnlinePlayers().forEach(online -> online.showPlayer(player));
                            }
                            if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Vanish: " + ChatColor.RED + "Off")) {
                                Bukkit.getServer().getOnlinePlayers().forEach(online -> online.hidePlayer(player));
                                ItemStack VANISH = new ItemBuilder(Material.INK_SACK, 1, (byte) 10).displayName(CC.YELLOW + "Vanish: " + CC.GREEN + "On").build();
                                player.setItemInHand(VANISH);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHit(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();

        if (!(e.getRightClicked() instanceof Player)) return;

        Player entity = (Player) e.getRightClicked();

        if (HCF.getPlugin().getStaffModeManager().getStaffMode().contains(player.getUniqueId())) {
            if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
                if (player.getItemInHand().getType() == Material.BOOK) {
                    HCF.getPlugin().getStaffModeManager().examine(player, entity);
                }
                if (player.getItemInHand().getType() == Material.PACKED_ICE) {
                    player.chat("/freeze " + entity.getName());
                }

            }
        }
    }

}
