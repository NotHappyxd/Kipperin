package me.happy.hcf.eventgame.koth;

import com.doctordark.util.command.ArgumentExecutor;
import me.happy.hcf.HCF;
import me.happy.hcf.eventgame.koth.argument.KothHelpArgument;
import me.happy.hcf.eventgame.koth.argument.KothNextArgument;
import me.happy.hcf.eventgame.koth.argument.KothScheduleArgument;
import me.happy.hcf.eventgame.koth.argument.KothSetCapDelayArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Command used to handle KingOfTheHills.
 */
public class KothExecutor extends ArgumentExecutor {

    private final KothScheduleArgument kothScheduleArgument;

    public KothExecutor(HCF plugin) {
        super("koth");

        addArgument(new KothHelpArgument(this));
        addArgument(new KothNextArgument(plugin));
        addArgument(this.kothScheduleArgument = new KothScheduleArgument(plugin));
        addArgument(new KothSetCapDelayArgument(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            this.kothScheduleArgument.onCommand(sender, command, label, args);
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
