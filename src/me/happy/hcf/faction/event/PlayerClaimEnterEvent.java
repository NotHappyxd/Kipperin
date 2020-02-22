package me.happy.hcf.faction.event;

import me.happy.hcf.faction.claim.Claim;
import me.happy.hcf.faction.type.Faction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Faction event called when a {@link Player} enters a different {@link Claim}.
 */
public class PlayerClaimEnterEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Faction fromFaction;
    private final Faction toFaction;
    private final Location from;
    private final Location to;
    private final EnterCause enterCause;
    private boolean cancelled;

    public PlayerClaimEnterEvent(Player player, Location from, Location to, Faction fromFaction, Faction toFaction, EnterCause enterCause) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.fromFaction = fromFaction;
        this.toFaction = toFaction;
        this.enterCause = enterCause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the {@link Faction} at the from {@link Location}.
     *
     * @return the {@link Faction} at the from {@link Location}
     */
    public Faction getFromFaction() {
        return fromFaction;
    }

    /**
     * Gets the {@link Faction} at the to {@link Location}.
     *
     * @return the {@link Faction} at the to {@link Location}
     */
    public Faction getToFaction() {
        return toFaction;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public EnterCause getEnterCause() {
        return enterCause;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public enum EnterCause {

        TELEPORT, MOVEMENT
    }
}