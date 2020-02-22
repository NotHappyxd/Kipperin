package me.happy.hcf.combatlog.event;

import me.happy.hcf.combatlog.type.LoggerEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggerRemovedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LoggerEntity loggerEntity;

    public LoggerRemovedEvent(LoggerEntity loggerEntity) {
        this.loggerEntity = loggerEntity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LoggerEntity getLoggerEntity() {
        return loggerEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
