package me.happy.hcf.timer;

import com.doctordark.util.command.ArgumentExecutor;
import me.happy.hcf.HCF;
import me.happy.hcf.timer.argument.TimerCheckArgument;
import me.happy.hcf.timer.argument.TimerSetArgument;

/**
 * Handles the execution and tab completion of the timer command.
 */
public class TimerExecutor extends ArgumentExecutor {

    public TimerExecutor(HCF plugin) {
        super("timer");

        addArgument(new TimerCheckArgument(plugin));
        addArgument(new TimerSetArgument(plugin));
    }
}