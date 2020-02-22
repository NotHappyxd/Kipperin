package me.happy.hcf.eventgame;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import me.happy.hcf.HCF;
import me.happy.hcf.eventgame.tracker.ConquestTracker;
import me.happy.hcf.eventgame.tracker.EventTracker;
import me.happy.hcf.eventgame.tracker.KothTracker;

public enum EventType {

    CONQUEST("Conquest", new ConquestTracker(HCF.getPlugin())), KOTH("KOTH", new KothTracker(HCF.getPlugin()));

    private static final ImmutableMap<String, EventType> byDisplayName;

    static {
        ImmutableMap.Builder<String, EventType> builder = new ImmutableBiMap.Builder<>();
        for (EventType eventType : values()) {
            builder.put(eventType.displayName.toLowerCase(), eventType);
        }

        byDisplayName = builder.build();
    }

    private final EventTracker eventTracker;
    private final String displayName;

    EventType(String displayName, EventTracker eventTracker) {
        this.displayName = displayName;
        this.eventTracker = eventTracker;
    }

    @Deprecated
    public static EventType getByDisplayName(String name) {
        return byDisplayName.get(name.toLowerCase());
    }

    public EventTracker getEventTracker() {
        return eventTracker;
    }

    public String getDisplayName() {
        return displayName;
    }
}
