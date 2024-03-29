package me.happy.hcf.eventgame.eotw;

import me.happy.hcf.HCF;
import me.happy.hcf.faction.event.FactionClaimChangeEvent;
import me.happy.hcf.faction.event.cause.ClaimChangeCause;
import me.happy.hcf.faction.type.Faction;
import me.happy.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener used to handle events for if EOTW is active.
 */
public class EotwListener implements Listener {

    private final HCF plugin;

    public EotwListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        EotwHandler.EotwRunnable runnable = plugin.getEotwHandler().getRunnable();
        if (runnable != null) runnable.handleDisconnect(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        EotwHandler.EotwRunnable runnable = plugin.getEotwHandler().getRunnable();
        if (runnable != null) runnable.handleDisconnect(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        EotwHandler.EotwRunnable runnable = plugin.getEotwHandler().getRunnable();
        if (runnable != null) runnable.handleDisconnect(event.getEntity());
    }

    /*
    Configurable please
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionCreate(FactionCreateEvent event) {
        if (plugin.getEotwHandler().isEndOfTheWorld()) {
            Faction faction = event.getFaction();
            if (faction instanceof PlayerFaction) {
                event.setCancelled(true);
                event.getSender().sendMessage(ChatColor.RED + "Player based factions cannot be created during EOTW.");
            }
        }
    }*/

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionClaimChange(FactionClaimChangeEvent event) {
        if (plugin.getEotwHandler().isEndOfTheWorld() && event.getCause() == ClaimChangeCause.CLAIM) {
            Faction faction = event.getClaimableFaction();
            if (faction instanceof PlayerFaction) {
                event.setCancelled(true);
                event.getSender().sendMessage(ChatColor.RED + "Player based faction land cannot be claimed during EOTW.");
            }
        }
    }
}
