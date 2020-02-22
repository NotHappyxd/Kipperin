package me.happy.hcf.faction.event;

import me.happy.hcf.faction.struct.Relation;
import me.happy.hcf.faction.type.Faction;
import me.happy.hcf.faction.type.PlayerFaction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Faction event called a {@link PlayerFaction} has removed a {@link Relation}
 * with another {@link PlayerFaction}.
 */
public class FactionRelationRemoveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerFaction senderFaction;
    private final PlayerFaction targetFaction;
    private final Relation relation;
    private boolean cancelled;

    public FactionRelationRemoveEvent(PlayerFaction senderFaction, PlayerFaction targetFaction, Relation relation) {
        this.senderFaction = senderFaction;
        this.targetFaction = targetFaction;
        this.relation = relation;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the {@link Faction} sending the request.
     *
     * @return the requesting {@link Faction}
     */
    public PlayerFaction getSenderFaction() {
        return senderFaction;
    }

    /**
     * Gets the {@link Faction} asked to accept request.
     *
     * @return the targeted {@link Faction}
     */
    public PlayerFaction getTargetFaction() {
        return targetFaction;
    }

    public Relation getRelation() {
        return relation;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}