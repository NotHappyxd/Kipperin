package me.happy.hcf;

import com.doctordark.base.BasePlugin;
import com.google.common.base.Joiner;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import me.happy.hcf.combatlog.type.CombatLogListener;
import me.happy.hcf.command.*;
import me.happy.hcf.deathban.*;
import me.happy.hcf.deathban.lives.LivesExecutor;
import me.happy.hcf.economy.*;
import me.happy.hcf.eventgame.CaptureZone;
import me.happy.hcf.eventgame.EventExecutor;
import me.happy.hcf.eventgame.EventScheduler;
import me.happy.hcf.eventgame.conquest.ConquestExecutor;
import me.happy.hcf.eventgame.eotw.EotwCommand;
import me.happy.hcf.eventgame.eotw.EotwHandler;
import me.happy.hcf.eventgame.eotw.EotwListener;
import me.happy.hcf.eventgame.faction.CapturableFaction;
import me.happy.hcf.eventgame.faction.ConquestFaction;
import me.happy.hcf.eventgame.faction.KothFaction;
import me.happy.hcf.eventgame.koth.KothExecutor;
import me.happy.hcf.faction.FactionExecutor;
import me.happy.hcf.faction.FactionManager;
import me.happy.hcf.faction.FactionMember;
import me.happy.hcf.faction.FlatFileFactionManager;
import me.happy.hcf.faction.claim.*;
import me.happy.hcf.faction.type.*;
import me.happy.hcf.files.MessageFile;
import me.happy.hcf.listener.*;
import me.happy.hcf.listener.fixes.*;
import me.happy.hcf.pvpclass.PvpClassManager;
import me.happy.hcf.pvpclass.bard.EffectRestorer;
import me.happy.hcf.scoreboard.ScoreboardHandler;
import me.happy.hcf.sotw.SotwCommand;
import me.happy.hcf.sotw.SotwListener;
import me.happy.hcf.sotw.SotwTimer;
import me.happy.hcf.staff.StaffModeCommand;
import me.happy.hcf.staff.StaffModeListener;
import me.happy.hcf.staff.StaffModeManager;
import me.happy.hcf.staff.freeze.FreezeCommand;
import me.happy.hcf.staff.freeze.FreezeListener;
import me.happy.hcf.staff.freeze.FreezeManager;
import me.happy.hcf.timer.TimerExecutor;
import me.happy.hcf.timer.TimerManager;
import me.happy.hcf.user.FactionUser;
import me.happy.hcf.user.UserManager;
import me.happy.hcf.visualise.ProtocolLibHook;
import me.happy.hcf.visualise.VisualiseHandler;
import me.happy.hcf.visualise.WallBorderHandler;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class HCF extends JavaPlugin {

    public static final Joiner SPACE_JOINER = Joiner.on(' ');
    public static final Joiner COMMA_JOINER = Joiner.on(", ");

    @Getter
    private static HCF plugin;

    @Getter
    private Random random = new Random();

    @Getter
    private Configuration configuration;

    @Getter
    private ClaimHandler claimHandler;

    @Getter
    private CombatLogListener combatLogListener;

    @Getter
    private DeathbanManager deathbanManager;

    @Getter
    private EconomyManager economyManager;

    @Getter
    private EffectRestorer effectRestorer;

    @Getter
    private EotwHandler eotwHandler;

    @Getter
    private EventScheduler eventScheduler;

    @Getter
    private FactionManager factionManager;

    @Getter
    private ImageFolder imageFolder;

    @Getter
    private PvpClassManager pvpClassManager;

    @Getter
    private ScoreboardHandler scoreboardHandler;

    @Getter
    private SotwTimer sotwTimer;

    @Getter
    private TimerManager timerManager;

    @Getter
    private UserManager userManager;

    @Getter
    private VisualiseHandler visualiseHandler;

    @Getter
    private WorldEditPlugin worldEdit;

    @Getter
    private boolean paperPatch;

    @Getter
    private Chat chat = null;

    @Getter
    private MessageFile messageFile;

    @Getter
    private BasePlugin basePlugin;

    @Getter
    private StaffModeManager staffModeManager;

    @Getter
    private FreezeManager freezeManager;

    private boolean configurationLoaded = true;

    @Override
    public void onEnable() {
        registerConfiguration();
        if (!configurationLoaded) {
            getLogger().severe("Disabling plugin..");
            setEnabled(false);
            return;
        }

        HCF.plugin = this;
        DateTimeFormats.reload(ConfigurationService.SERVER_TIME_ZONE);        // Initialise the static fields.
        ///////////////////////////
        Plugin wep = getServer().getPluginManager().getPlugin("WorldEdit");  // Initialise WorldEdit hook.
        worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;

        this.setupChat();

        getConfig().options().copyDefaults(true);
        saveConfig();
        MessageFile.init(this);

        ConfigurationService.init(getConfig());

        registerSerialization();
        registerManagersBase();
        registerCommands();
        registerManagers();
        registerListeners();

        paperPatch = false;

        //TODO: More reliable, SQL based.
        long dataSaveInterval = TimeUnit.MINUTES.toMillis(20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                saveData();
            }
        }.runTaskTimerAsynchronously(this, dataSaveInterval, dataSaveInterval);

        ProtocolLibHook.hook(this); // Initialise ProtocolLib hook.


    }

    private void saveData() {
        deathbanManager.saveDeathbanData();
        economyManager.saveEconomyData();
        factionManager.saveFactionData();
        timerManager.saveTimerData();
        userManager.saveUserData();
    }

    @Override
    public void onDisable() {
        if (!configurationLoaded) {
            // Ignore everything.
            return;
        }

        try {
            String configFileName = "config.cdl";
            configuration.save(new File(getDataFolder(), configFileName), HCF.class.getResource("/" + configFileName));
        } catch (IOException | InvalidConfigurationException ex) {
            getLogger().warning("Unable to save config.");
            ex.printStackTrace();
        }

        combatLogListener.removeCombatLoggers();
        pvpClassManager.onDisable();
        scoreboardHandler.clearBoards();

        saveData();
        basePlugin.disable();
        basePlugin = null;
        HCF.plugin = null; // Always uninitialise last.
    }

    private void registerConfiguration() {
        configuration = new Configuration();
        try {
            String configFileName = "config.cdl";
            File file = new File(getDataFolder(), configFileName);
            if (!file.exists()) {
                saveResource(configFileName, false);
            }

            configuration.load(file, HCF.class.getResource("/" + configFileName));
            configuration.updateFields();
        } catch (IOException | InvalidConfigurationException ex) {
            getLogger().log(Level.SEVERE, "Failed to load configuration", ex);
            configurationLoaded = false;
        }
    }

    //TODO: More reliable, SQL based.
    private void registerSerialization() {
        ConfigurationSerialization.registerClass(CaptureZone.class);
        ConfigurationSerialization.registerClass(Deathban.class);
        ConfigurationSerialization.registerClass(Claim.class);
        ConfigurationSerialization.registerClass(Subclaim.class);
        ConfigurationSerialization.registerClass(Deathban.class);
        ConfigurationSerialization.registerClass(FactionUser.class);
        ConfigurationSerialization.registerClass(ClaimableFaction.class);
        ConfigurationSerialization.registerClass(ConquestFaction.class);
        ConfigurationSerialization.registerClass(CapturableFaction.class);
        ConfigurationSerialization.registerClass(KothFaction.class);
        ConfigurationSerialization.registerClass(EndPortalFaction.class);
        ConfigurationSerialization.registerClass(Faction.class);
        ConfigurationSerialization.registerClass(FactionMember.class);
        ConfigurationSerialization.registerClass(PlayerFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.class);
        ConfigurationSerialization.registerClass(SpawnFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
    }

    private void registerListeners() {
        Arrays.asList(
                new BlockHitFixListener(),
                new BlockJumpGlitchFixListener(),
                new BoatGlitchFixListener(this),
                new BookDisenchantListener(this),
                new BottledExpListener(this),
                new ChatListener(this),
                new ClaimWandListener(this),
                combatLogListener = new CombatLogListener(this),
                new CoreListener(this),
                new CrowbarListener(this),
                new DeathListener(this),
                new DeathbanListener(this),
                new EnchantLimitListener(this),
                new EnderChestRemovalListener(this),
                new EntityLimitListener(this),
                new EotwListener(this),
                new EventSignListener(),
                new ExpMultiplierListener(this),
                new FactionListener(this),
                new FurnaceSmeltSpeedListener(this),
                new InfinityArrowFixListener(this),
                // new PearlGlitchListener(this),
                new PortalListener(this),
                new PotionLimitListener(this),
                new ProtectionListener(this),
                new SubclaimWandListener(this),
                new SignSubclaimListener(this),
                new ShopSignListener(this),
                new SkullListener(),
                new SotwListener(this),
                new BeaconStrengthFixListener(this),
                new VoidGlitchFixListener(),
                new WallBorderHandler(),
                new WorldListener(),
                new ParticleTrailListener(),
                new StaffModeListener(),
                new FreezeListener(),
                new SnowballSwapListener(this),
                new GrapplingHookListener(),
                new ElevatorListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        getCommand("angle").setExecutor(new AngleCommand());
        getCommand("conquest").setExecutor(new ConquestExecutor(this));
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("eotw").setExecutor(new EotwCommand(this));
        getCommand("event").setExecutor(new EventExecutor(this));
        getCommand("faction").setExecutor(new FactionExecutor(this));
        getCommand("gopple").setExecutor(new GoppleCommand(this));
        getCommand("koth").setExecutor(new KothExecutor(this));
        getCommand("lives").setExecutor(new LivesExecutor(this));
        getCommand("location").setExecutor(new LocationCommand(this));
        getCommand("logout").setExecutor(new LogoutCommand(this));
        getCommand("mapkit").setExecutor(new MapKitCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("pvptimer").setExecutor(new PvpTimerCommand(this));
        getCommand("regen").setExecutor(new RegenCommand(this));
        getCommand("servertime").setExecutor(new ServerTimeCommand(this));
        getCommand("sotw").setExecutor(new SotwCommand(this));
        getCommand("spawncannon").setExecutor(new SpawnCannonCommand(this));
        getCommand("staffrevive").setExecutor(new StaffReviveCommand(this));
        getCommand("timer").setExecutor(new TimerExecutor(this));
        getCommand("togglecapzoneentry").setExecutor(new ToggleCapzoneEntryCommand(this));
        getCommand("togglelightning").setExecutor(new ToggleLightningCommand(this));
        getCommand("togglesidebar").setExecutor(new ToggleSidebarCommand(this));
        getCommand("kipperin").setExecutor(new KipperinCommand());
        getCommand("trail").setExecutor(new TrailCommand());
        getCommand("staff").setExecutor(new StaffModeCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("specialitems").setExecutor(new SpecialItemsCommand());
        getCommand("demomenu").setExecutor(new DemoMenuCommand());

        Map<String, Map<String, Object>> map = getDescription().getCommands();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            PluginCommand command = getCommand(entry.getKey());
            command.setPermission("hcf.command." + entry.getKey());
            command.setPermissionMessage(ChatColor.RED + "You do not have permission for this command.");
        }
    }

    private void registerManagers() {
        claimHandler = new ClaimHandler(this);
        deathbanManager = new FlatFileDeathbanManager(this);
        economyManager = new FlatFileEconomyManager(this);
        effectRestorer = new EffectRestorer(this);
        eotwHandler = new EotwHandler(this);
        eventScheduler = new EventScheduler(this);
        factionManager = new FlatFileFactionManager(this);
        imageFolder = new ImageFolder(this);
        pvpClassManager = new PvpClassManager(this);
        sotwTimer = new SotwTimer();
        timerManager = new TimerManager(this); // Needs to be registered before ScoreboardHandler.
        scoreboardHandler = new ScoreboardHandler(this);
        userManager = new UserManager(this);
        visualiseHandler = new VisualiseHandler();
        staffModeManager = new StaffModeManager();
        this.freezeManager = new FreezeManager(this);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private void registerManagersBase() {
        this.basePlugin = BasePlugin.getPlugin();
        this.basePlugin.init(this);
    }
}
