package me.happy.hcf.files;

import com.doctordark.util.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MessageFile {

    private static Config config;

    public static void init(JavaPlugin plugin) {
        config = new Config(plugin, "messages");
    }

    private static void reloadFile() {
        File file = new File("messages.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    }

    public static Config getConfig() {
        return config;
    }
}
