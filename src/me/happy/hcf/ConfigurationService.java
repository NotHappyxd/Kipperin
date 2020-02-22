package me.happy.hcf;

import com.doctordark.util.GenericUtils;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public final class ConfigurationService {

    public static final int END_PORTAL_RADIUS = 20;
    public static final int END_PORTAL_CENTER = 500;
    public static final Map<World.Environment, Integer> ROAD_LENGTHS = new EnumMap<>(World.Environment.class);
    public static final Map<World.Environment, Integer> SPAWN_RADIUS_MAP = new EnumMap<>(World.Environment.class);

    public static String SCOREBOARD_TITLE;
    public static double GLOBAL_XP_MULTIPLIER;
    public static double FISHING_XP_MULTIPLIER;
    public static double SMELTING_XP_MULTIPLIER;
    public static double LOOTING_PER_LEVEL_XP_MULTIPLIER;
    public static double LUCK_PER_LEVEL_XP_MULTIPLIER;
    public static double FORTUNE_PER_LEVEL_XP_MULTIPLIER;
    public static TimeZone SERVER_TIME_ZONE;
    public static boolean DISABLE_ENDERCHEST;
    public static Map<Enchantment, Integer> ENCHANTMENT_LIMITS;
    public static Map<PotionType, Integer> POTION_LIMIT;
    public static int MAX_ALLIES;
    public static int MAX_MEMBERS;
    public static boolean KITMAP;

    static {
        ROAD_LENGTHS.put(World.Environment.NORMAL, 4000);
        ROAD_LENGTHS.put(World.Environment.NETHER, 4000);

        SPAWN_RADIUS_MAP.put(World.Environment.NORMAL, 50);
        SPAWN_RADIUS_MAP.put(World.Environment.NETHER, 25);
        SPAWN_RADIUS_MAP.put(World.Environment.THE_END, 15);
    }

    public static void init(MemorySection memorySection) {
        SCOREBOARD_TITLE = memorySection.getString("scoreboard-title");
        GLOBAL_XP_MULTIPLIER = memorySection.getDouble("expMultiplier.global");
        FISHING_XP_MULTIPLIER = memorySection.getDouble("expMultiplier.fishing");
        SMELTING_XP_MULTIPLIER = memorySection.getDouble("expMultiplier.smelting");
        LOOTING_PER_LEVEL_XP_MULTIPLIER = memorySection.getDouble("expMultiplier.lootingPerLevel");
        LUCK_PER_LEVEL_XP_MULTIPLIER = memorySection.getDouble("expMultiplier.luckPerLevel");
        FORTUNE_PER_LEVEL_XP_MULTIPLIER = memorySection.getDouble("expMultiplier.fortunePerLevel");
        SERVER_TIME_ZONE = TimeZone.getTimeZone(memorySection.getString("timezone"));
        DISABLE_ENDERCHEST = memorySection.getBoolean("disable-enderchest");
        MAX_ALLIES = memorySection.getInt("max-allies");
        MAX_MEMBERS = memorySection.getInt("max-members");
        KITMAP = memorySection.getBoolean("kitmap");

        //REGISTER ENCHANTMENT LIMIT AND POTION LIMITS
        setUpEnchantsAndPotions(memorySection);


    }

    private static void setUpEnchantsAndPotions(MemorySection memorySection) {
        ENCHANTMENT_LIMITS = new HashMap<>();
        for (Map.Entry<String, Integer> entry : GenericUtils.castMap(memorySection.get("enchantment-limits"), String.class, Integer.class).entrySet()) {
            Enchantment enchantment = Enchantment.getByName(entry.getKey().toUpperCase());
            if (enchantment != null) {
                ENCHANTMENT_LIMITS.put(enchantment, entry.getValue());
                HCF.getPlugin().getLogger().info("Using enchantment limit " + enchantment.getName() + " " + entry.getValue());
            } else {
                HCF.getPlugin().getLogger().severe("Failed to find enchantment " + entry.getKey());
            }
        }

        for (Enchantment enchantment : Enchantment.values()) {
            if (!ENCHANTMENT_LIMITS.containsKey(enchantment)) {
                ENCHANTMENT_LIMITS.put(enchantment, enchantment.getMaxLevel());
            }
        }

        POTION_LIMIT = new HashMap<>();
        for (Map.Entry<String, Integer> entry : GenericUtils.castMap(memorySection.get("enchantment-limits"), String.class, Integer.class).entrySet()) {
            PotionType potionType = PotionType.valueOf(entry.getKey().toUpperCase());
            if (potionType != null) {
                POTION_LIMIT.put(potionType, entry.getValue());
                HCF.getPlugin().getLogger().info("Using potion limit " + potionType.name() + " " + entry.getValue());
            } else {
                HCF.getPlugin().getLogger().severe("Failed to find potion " + entry.getKey());
            }
        }

        for (PotionType potionType : PotionType.values()) {
            if (!POTION_LIMIT.containsKey(potionType)) {
                POTION_LIMIT.put(potionType, potionType.getMaxLevel());
            }
        }
    }
}
