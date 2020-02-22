package me.happy.hcf.sotw;

import me.happy.hcf.HCF;
import me.happy.hcf.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SotwListener implements Listener {

    private final HCF plugin;


    public SotwListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityAttack(EntityDamageByEntityEvent e) {
        if (plugin.getSotwTimer().getSotwRunnable() != null) {
            if (e.getEntity() instanceof Player) {
                if (e.getDamager() instanceof Projectile && !(((Projectile) e.getDamager()).getShooter() instanceof Player))
                    return;
                if (!(e.getDamager() instanceof Player)) return;

                // HANDLE BOWS AND STUFF RIGHT HERE
                Player player = (Player) e.getEntity();

                if (!plugin.getSotwTimer().getEnabled().contains(player.getUniqueId())) e.setCancelled(true);

                if (e.getDamager() instanceof Projectile) {
                    Player shooter = (Player) ((Projectile) e.getDamager()).getShooter();

                    if (!plugin.getSotwTimer().getEnabled().contains(shooter.getUniqueId())) {
                        e.setCancelled(true);
                        shooter.sendMessage(ChatColor.RED + "You need to enable your SOTW timer!");
                    }
                }

                Player damager = (Player) e.getDamager();

                if (!plugin.getSotwTimer().getEnabled().contains(damager.getUniqueId())) {
                    damager.sendMessage(CC.RED + "You need to enable your SOTW timer!");
                }
            }
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() != EntityDamageEvent.DamageCause.SUICIDE && plugin.getSotwTimer().getSotwRunnable() != null) {
            event.setCancelled(true);
        }
    }
}
