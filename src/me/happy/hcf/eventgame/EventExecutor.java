package me.happy.hcf.eventgame;

import com.doctordark.util.command.ArgumentExecutor;
import me.happy.hcf.HCF;
import me.happy.hcf.eventgame.argument.*;

/**
 * Handles the execution and tab completion of the event command.
 */
public class EventExecutor extends ArgumentExecutor {

    public EventExecutor(HCF plugin) {
        super("event");

        addArgument(new EventCancelArgument(plugin));
        addArgument(new EventCreateArgument(plugin));
        addArgument(new EventDeleteArgument(plugin));
        addArgument(new EventRenameArgument(plugin));
        addArgument(new EventSetAreaArgument(plugin));
        addArgument(new EventSetCapzoneArgument(plugin));
        addArgument(new EventStartArgument(plugin));
        addArgument(new EventUptimeArgument(plugin));
    }
}