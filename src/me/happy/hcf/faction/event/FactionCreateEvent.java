package me.happy.hcf.faction.event;

import me.happy.hcf.faction.type.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event called when a {@link Faction} is about to be created.
 */
public class FactionCreateEvent extends FactionEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final CommandSender sender;
    private boolean cancelled;

    public FactionCreateEvent(Faction faction, CommandSender sender) {
        super(faction);
        this.sender = sender;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the {@link CommandSender} attempting to create this {@link Faction}.
     *
     * @return the {@link CommandSender}
     */
    public CommandSender getSender() {
        return sender;
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
}
