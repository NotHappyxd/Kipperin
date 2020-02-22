package me.happy.hcf.staff.freeze;

import me.happy.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class FreezeListener implements Listener {

    private HCF plugin = HCF.getPlugin();
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getDamager().getUniqueId())) {
            e.setCancelled(true);
            ((Player) e.getDamager()).sendMessage(ChatColor.RED + "You cannot attack players while frozen.");
        }
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            ((Player) e.getDamager()).sendMessage(ChatColor.RED + ((Player)e.getEntity()).getName() + " is currently frozen. You cannnot attack frozen players.");
        }
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot place blocks when you are frozen.");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot break blocks when you are frozen.");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getPlayer().getUniqueId())) {
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                e.setTo(e.getFrom());
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (plugin.getFreezeManager().getFrozen().containsKey(((Player)e.getWhoClicked()).getUniqueId())) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if(!(event.getPlayer() instanceof Player)){
            return; //Better safe then sorry
        }
        
        Player player = (Player) event.getPlayer();
        if(player.isOnline() && plugin.getFreezeManager().getState(player) == FreezeState.GUI){
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if(player.isOnline() && plugin.getFreezeManager().getState(player) == FreezeState.GUI){
                    player.openInventory(plugin.getFreezeManager().getInv());
                }
            });
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot drop items while you are frozen.");
        }
    }

    @EventHandler
    public void onDrop(PlayerPickupItemEvent e) {
        if (plugin.getFreezeManager().getFrozen().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
