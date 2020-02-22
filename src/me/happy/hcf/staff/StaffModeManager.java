package me.happy.hcf.staff;

import com.doctordark.util.ItemBuilder;
import lombok.Getter;
import me.happy.hcf.HCF;
import me.happy.hcf.util.CC;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

@Getter
public class StaffModeManager {

    private Map<UUID, ItemStack[]> armorMap = new HashMap<>();
    private Map<UUID, ItemStack[]> inventoryMap = new HashMap<>();
    private Set<UUID> staffMode = new HashSet<>();
    private Set<UUID> vanished = new HashSet<>();

    public void giveItem(Player player) {
        ItemStack COMPASS = new ItemBuilder(Material.COMPASS).displayName(CC.YELLOW + "Teleporter").build();
        ItemStack EXAMINE = new ItemBuilder(Material.BOOK).displayName(CC.RED + "Examine").lore(CC.RED + "Allows you to see someones inventory").build();
        ItemStack WORLDEDIT = new ItemBuilder(Material.WOOD_AXE).displayName(CC.L_PURPLE + "WorldEdit Wand").build();
        ItemStack FREEZE = new ItemBuilder(Material.PACKED_ICE).displayName(CC.YELLOW + "Freeze").lore(CC.AQUA + "Freezes someone who you click").build();
        ItemStack RANDOM_TP = new ItemBuilder(Material.GREEN_RECORD).displayName(CC.GREEN + "Random TP").lore(CC.GREEN + "Randomly tps you to someone on the server").build();
        ItemStack XRAY_TP = new ItemBuilder(Material.DIAMOND_PICKAXE).displayName(CC.AQUA + "XRay TP").lore(CC.AQUA + "Allows you to teleport to anyone below y 40").build();
        ItemStack VANISH = new ItemBuilder(Material.INK_SACK, 1, (byte) 10).displayName(CC.YELLOW + "Vanish: " + CC.GREEN + "On").lore(ChatColor.GREEN + "Lets you toggle your vanish!").build();

        player.getInventory().setItem(0, COMPASS);
        player.getInventory().setItem(1, EXAMINE);
        player.getInventory().setItem(2, WORLDEDIT);
        player.getInventory().setItem(4, FREEZE);
        player.getInventory().setItem(6, RANDOM_TP);
        player.getInventory().setItem(7, XRAY_TP);
        player.getInventory().setItem(8, VANISH);
    }

    public void examine(Player player, Player target) {
        Inventory inventory = Bukkit.getServer().createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&eInspecting: " + target.getName()));
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HCF.getPlugin(), new Runnable() {
            public void run() {
                PlayerInventory playerInventory = target.getInventory();

                ItemStack cookedBeef = new ItemBuilder(Material.COOKED_BEEF, target.getFoodLevel()).displayName(ChatColor.RED + "Hunger").lore(ChatColor.RED.toString() + target.getFoodLevel() + "/20").build();

                ItemStack brewingStand = new ItemStack(Material.BREWING_STAND_ITEM, target.getPlayer().getActivePotionEffects().size());
                ItemMeta brewingStandMeta = brewingStand.getItemMeta();
                brewingStandMeta.setDisplayName(CC.translate("&eActive Effects"));
                ArrayList<String> brewingStandLore = new ArrayList<String>();
                for (PotionEffect potionEffect : target.getPlayer().getActivePotionEffects()) {
                    String effectName = potionEffect.getType().getName();
                    int effectLevel = potionEffect.getAmplifier();
                    effectLevel++;
                    brewingStandLore.add(CC.translate("&e" + WordUtils.capitalizeFully(effectName).replace("_", " ") + " " + effectLevel + "&7: &c" + setFormat(potionEffect.getDuration() / 20)));
                }
                brewingStandMeta.setLore(brewingStandLore);
                brewingStand.setItemMeta(brewingStandMeta);

                ItemStack compass = new ItemStack(Material.COMPASS, 1);
                ItemMeta compassMeta = compass.getItemMeta();
                compassMeta.setDisplayName(CC.translate("&ePlayer Location"));
                compassMeta.setLore(CC.translateLines(Arrays.asList("&eWorld&7: &e" + player.getWorld().getName(),"&eCoords", "  &eX&7: &c" + target.getLocation().getBlockX(), "  &eY&7: &c" + target.getLocation().getBlockY(), "  &eZ&7: &c" + target.getLocation().getBlockZ())));
                compass.setItemMeta(compassMeta);

                ItemStack heart = new ItemBuilder(Material.SPECKLED_MELON, (int) player.getHealth()).displayName(ChatColor.RED + "Health").lore(ChatColor.RED.toString() + player.getHealth() + "/20").build();
                inventory.setContents(playerInventory.getContents());
                inventory.setItem(36, playerInventory.getHelmet());
                inventory.setItem(37, playerInventory.getChestplate());
                inventory.setItem(38, playerInventory.getLeggings());
                inventory.setItem(39, playerInventory.getBoots());
                inventory.setItem(40, playerInventory.getItemInHand());
                inventory.setItem(41, heart);
                inventory.setItem(42, cookedBeef);
                inventory.setItem(43, brewingStand);
                inventory.setItem(44, compass);

            }
        }, 0, 5);
        player.openInventory(inventory);
    }

    private String setFormat(Integer value)
    {
        int remainder = value * 1000;

        int seconds = remainder / 1000 % 60;
        int minutes = remainder / 60000 % 60;
        int hours = remainder / 3600000 % 24;

        return (hours > 0 ? String.format("%02d:", hours) : "") + String.format("%02d:%02d", minutes, seconds);
    }
}
