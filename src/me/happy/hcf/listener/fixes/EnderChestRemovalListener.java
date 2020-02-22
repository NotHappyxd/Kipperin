package me.happy.hcf.listener.fixes;

import me.happy.hcf.ConfigurationService;
import me.happy.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.EnderChest;

import java.util.Iterator;

/**
 * Listener that prevents the use of Ender Chests.
 */
public class EnderChestRemovalListener implements Listener {

    private final HCF plugin;

    public EnderChestRemovalListener(HCF plugin) {
        this.plugin = plugin;
        if (ConfigurationService.DISABLE_ENDERCHEST) {
            this.removeRecipe();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEnderChestOpen(PlayerInteractEvent event) {
        if (ConfigurationService.DISABLE_ENDERCHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (ConfigurationService.DISABLE_ENDERCHEST && event.getInventory() instanceof EnderChest) {
            event.setCancelled(true);
        }
    }

    /**
     * Removes the ender-chest crafting recipe from the server meaning
     * it can only be obtained from creative mode.
     */
    private void removeRecipe() {
        for (Iterator<Recipe> iterator = Bukkit.recipeIterator(); iterator.hasNext(); ) {
            if (iterator.next().getResult().getType() == Material.ENDER_CHEST) {
                iterator.remove();
            }
        }
    }
}
