package me.happy.hcf.staff.freeze;

import com.doctordark.util.ItemBuilder;
import lombok.Getter;
import me.happy.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class FreezeManager {
    private Map<UUID, FreezeState> frozen = new HashMap<>();
    private Inventory inv;

    public FreezeManager(HCF plugin) {
        inv = plugin.getServer().createInventory(null, 9, ChatColor.YELLOW + "You are frozen!");
        inv.setItem(4, new ItemBuilder(Material.PAPER).displayName(ChatColor.RED + "You are frozen!").build());
    }

    public void setState(Player player, FreezeState state) {
        if (state == FreezeState.NONE) {
            frozen.remove(player.getUniqueId());
        }

        if (state == FreezeState.GUI) {
            player.closeInventory();
            player.openInventory(inv);
            frozen.put(player.getUniqueId(), state);
        }

        if (state == FreezeState.NO_GUI) {
            player.closeInventory();
            frozen.put(player.getUniqueId(), state);
        }
    }

    public FreezeState getState(Player player) {
        return frozen.getOrDefault(player.getUniqueId(), FreezeState.NONE);
    }
}
