package me.happy.hcf.listener;

import me.happy.hcf.HCF;
import me.happy.hcf.user.FactionUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParticleTrailListener implements Listener {

    @EventHandler
    public void onWalk(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        FactionUser playerData = HCF.getPlugin().getUserManager().getUser(player.getUniqueId());

        if (playerData.getTrail() != null) {
            player.getWorld().playEffect(player.getLocation(), playerData.getTrail(), 100);
        }
    }
}
