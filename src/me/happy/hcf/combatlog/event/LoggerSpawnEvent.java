package me.happy.hcf.combatlog.event;

import me.happy.hcf.combatlog.type.LoggerEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggerSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final LoggerEntity loggerEntity;
    private boolean cancelled;

    public LoggerSpawnEvent(LoggerEntity loggerEntity) {
        this.loggerEntity = loggerEntity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LoggerEntity getLoggerEntity() {
        return loggerEntity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
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
