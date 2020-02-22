package me.happy.hcf.combatlog.type;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggerSpawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final LoggerEntity loggerEntity;

    public LoggerSpawnEvent(LoggerEntity loggerEntity) {
        this.loggerEntity = loggerEntity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LoggerEntity getLoggerEntity() {
        return this.loggerEntity;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}