package me.happy.hcf.visualise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.happy.hcf.HCF;
import me.happy.hcf.packetwrapper.WrapperPlayClientBlockDig;
import me.happy.hcf.util.NmsUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ProtocolLibHook
{
    private static final int STARTED_DIGGING = 0;
    private static final int FINISHED_DIGGING = 2;

    public static void hook(final HCF hcf)
    {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(hcf, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_PLACE)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                StructureModifier<Integer> modifier = event.getPacket().getIntegers();
                Player player = event.getPlayer();
                try
                {
                    int face = modifier.read(0);
                    if (face == 255) {
                        return;
                    }
                    BlockPosition blockPosition = event.getPacket().getBlockPositionModifier().readSafely(0);
                    Location clickedBlock = new Location(player.getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                    if (hcf.getVisualiseHandler().getVisualBlockAt(player, clickedBlock) != null)
                    {
                        Location placedLocation = clickedBlock.clone();
                        switch (face)
                        {
                            case 2:
                                placedLocation.add(0.0D, 0.0D, -1.0D);
                                break;
                            case 3:
                                placedLocation.add(0.0D, 0.0D, 1.0D);
                                break;
                            case 4:
                                placedLocation.add(-1.0D, 0.0D, 0.0D);
                                break;
                            case 5:
                                placedLocation.add(1.0D, 0.0D, 0.0D);
                                break;
                            default:
                                return;
                        }
                        if (hcf.getVisualiseHandler().getVisualBlockAt(player, placedLocation) == null)
                        {
                            event.setCancelled(true);
                            player.sendBlockChange(placedLocation, Material.AIR, (byte)0);
                            NmsUtils.resendHeldItemPacket(player);
                        }
                    }
                }
                catch (FieldAccessException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(hcf, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG)
        {
            public void onPacketReceiving(PacketEvent event) {

                WrapperPlayClientBlockDig clientBlockDig = new WrapperPlayClientBlockDig(event.getPacket());
                int status = clientBlockDig.getStatus().ordinal();
                Player player = event.getPlayer();
                int x = clientBlockDig.getLocation().getX();
                int y = clientBlockDig.getLocation().getY();
                int z = clientBlockDig.getLocation().getZ();
                Location location = new Location(player.getWorld(), x, y, z);
                VisualBlock visualBlock = hcf.getVisualiseHandler().getVisualBlockAt(player, location);
                if (visualBlock != null) {
                    event.setCancelled(true);
                    VisualBlockData data = visualBlock.getBlockData();
                    if (status == 2 || status == 0) {
                        player.sendBlockChange(location, data.getBlockType(), data.getData());
                    } else if (status == 1) {
                        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                        if ((player.getGameMode() == GameMode.CREATIVE) || (entityPlayer.world.getType(new net.minecraft.server.v1_8_R3.BlockPosition(x, y, z)).getBlock().getDamage(entityPlayer, entityPlayer.world, new net.minecraft.server.v1_8_R3.BlockPosition(x, y, z)) >= 1.0F)) {
                            player.sendBlockChange(location, data.getBlockType(), data.getData());
                        }
                    }
                }
            }
        });
    }
}
