package me.happy.hcf.command;

import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DemoMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player target = Bukkit.getPlayer(strings[0]);
        PacketPlayOutGameStateChange gameStateChange = new PacketPlayOutGameStateChange(5, 0);
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(gameStateChange);
        return true;
    }
}
