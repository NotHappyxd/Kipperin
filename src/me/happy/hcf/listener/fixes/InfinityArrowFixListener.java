package me.happy.hcf.listener.fixes;

import me.happy.hcf.HCF;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Listener that removes {@link org.bukkit.entity.Arrow}s from bows with Infinity when they land.
 */
public class InfinityArrowFixListener implements Listener {

    private final HCF plugin;

    public InfinityArrowFixListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (plugin.getConfiguration().isRemoveInfinityArrowsOnLand()) {
            Entity entity = event.getEntity();
            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if (!(arrow.getShooter() instanceof Player) || ((CraftArrow) arrow).getHandle().fromPlayer == 2) {
                    arrow.remove();
                }
            }
        }
    }
}
