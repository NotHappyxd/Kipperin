package me.happy.hcf.listener;

import com.doctordark.util.ItemBuilder;
import me.happy.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class SnowballSwapListener implements Listener {

    private Set<Snowball> passive = new HashSet<>();

    private HCF plugin;

    public SnowballSwapListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjThrow(final ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Snowball) {
            Player shooter = (Player) e.getEntity().getShooter();
            if (shooter.getItemInHand().getItemMeta().hasDisplayName() && shooter.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Snowball Swapper") && shooter.getItemInHand().getItemMeta().hasLore() &&
                    shooter.getItemInHand().getItemMeta().getLore().contains(ChatColor.GRAY + "If this hits someone, you swap locations")) {
                passive.add((Snowball) e.getEntity());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
    public void snowball(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
            final Snowball s = (Snowball) e.getDamager();
            if (!passive.contains(s)) return;
            if (e.isCancelled()) return;
            if (s.getShooter() instanceof Player) {
                final Player shooter = (Player) s.getShooter();
                final Player shot = (Player) e.getEntity();
                final Location shooterLoc = shooter.getLocation();
                final ItemStack SWAPPER = new ItemBuilder(Material.SNOW_BALL).displayName(ChatColor.AQUA + "Snowball Swapper").lore(ChatColor.GRAY + "If this hits someone, you swap locations", ChatColor.GRAY + "Note it has a 8 block limit.").build();


                if (shooter.getLocation().distance(shot.getLocation()) > 8.0) {
                    shooter.sendMessage(ChatColor.RED + "You are not within 8 blocks of the target");
                    shooter.getInventory().addItem(SWAPPER);
                    return;
                }

                shooter.teleport(e.getEntity().getLocation());
                shot.teleport(shooterLoc);

                passive.remove(s);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            Location location = e.getEntity().getLocation();
            System.out.println(location.getBlock());
        }
    }
}
