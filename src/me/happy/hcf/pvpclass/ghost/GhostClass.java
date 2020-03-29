package me.happy.hcf.pvpclass.ghost;

import me.happy.hcf.pvpclass.PvpClass;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class GhostClass extends PvpClass implements Listener {

    public GhostClass() {
        super("Ghost", TimeUnit.SECONDS.toMillis(4L));
    }

    @EventHandler
    public void onEffectGive(PotionEffectAddEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (e.getEffect().getType() == PotionEffectType.INVISIBILITY) {
                PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
                PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
                PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
                PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(helmetPacket);
                    ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(chestPacket);
                    ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(legPacket);
                    ((CraftPlayer)player1).getHandle().playerConnection.sendPacket(bootsPacket);
                }
            }
        }
    }
    @Override
    public boolean isApplicableFor(Player player) {
        PlayerInventory inv = player.getInventory();

        ItemStack helmet = inv.getHelmet();
        if (helmet == null || helmet.getType() != Material.LEATHER_HELMET) return false;

        ItemStack chestplate = inv.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.LEATHER_CHESTPLATE) return false;

        ItemStack leggings = inv.getLeggings();
        if (leggings == null || leggings.getType() != Material.CHAINMAIL_CHESTPLATE) return false;

        ItemStack boots = inv.getBoots();
        return !(boots == null || boots.getType() != Material.CHAINMAIL_BOOTS);
    }
}
