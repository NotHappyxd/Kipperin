package me.happy.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class GrapplingHookListener implements Listener {

    // private ArrayList<Player> cooldown = new ArrayList<Player>(), nofall = new ArrayList<Player>();

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.IN_GROUND || e.getState() == org.bukkit.event.player.PlayerFishEvent.State.FAILED_ATTEMPT) {
            if (e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName() &&
                    e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Grappling Hook")
                    && e.getPlayer().getItemInHand().getItemMeta().hasLore() && e.getPlayer().getItemInHand().getItemMeta().getLore().contains(ChatColor.YELLOW + "Propel yourself!")) {
                if (e.getHook().getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR || e.getHook().getLocation().getBlock().getType() != Material.AIR) {
                    System.out.println(e.getHook().getLocation().subtract(0, 1, 0).getBlock());
                    final Player player = e.getPlayer();
                    Entity entity = e.getHook();
                    Location loc = e.getHook().getLocation();
                    if (player.equals(entity)) { //the player is pulling themself to a location
                        {
                            pullEntityToLocation(player, loc);
                        }
                    }
                }
            }
        }
    }


    private void pullEntityToLocation(final Entity e, Location loc){
        Location entityLoc = e.getLocation();

        entityLoc.setY(entityLoc.getY()+0.5);
        e.teleport(entityLoc);

        double g = -0.08;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0+0.07*t) * (loc.getX()-entityLoc.getX())/t;
        double v_y = (1.0+0.03*t) * (loc.getY()-entityLoc.getY())/t -0.5*g*t;
        double v_z = (1.0+0.07*t) * (loc.getZ()-entityLoc.getZ())/t;

        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setZ(v_z);
        v.setY(v_y);

        e.setVelocity(v.multiply(1.3D));
    }
}
