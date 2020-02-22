package me.happy.hcf;

import com.doctordark.util.PersistableLocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.techcable.techutils.config.AnnotationConfig;
import net.techcable.techutils.config.Setting;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class Configuration extends AnnotationConfig {

    @Setting("factions.home.allowTeleportingInEnemyTerritory")
    private boolean allowTeleportingInEnemyTerritory = true;

    @Setting("handleEntityLimiting")
    private boolean handleEntityLimiting = true;

    @Setting("removeInfinityArrowsOnLand")
    private boolean removeInfinityArrowsOnLand = true;

    @Setting("beaconStrengthLevelLimit")
    private int beaconStrengthLevelLimit = 1;

    @Setting("disableBoatPlacementOnLand")
    private boolean disableBoatPlacementOnLand = true;

    @Setting("preventPlacingBedsNether")
    private boolean preventPlacingBedsNether = false;

    @Setting("furnaceCookSpeedMultiplier")
    private float furnaceCookSpeedMultiplier = 6.0F;

    @Setting("bottledExp")
    private boolean bottledExp = true;

    @Setting("bookDisenchanting")
    private boolean bookDisenchanting = true;

    @Setting("deathSigns")
    private boolean deathSigns = true;

    @Setting("deathLightning")
    private boolean deathLightning = true;

    @Setting("preventAllyDamage")
    private boolean preventAllyAttackDamage = true;

    @Setting("economy.startingBalance")
    private int economyStartingBalance = 250;

    @Setting("spawners.preventBreakingNether")
    private boolean spawnersPreventBreakingNether = true;

    @Setting("spawners.preventPlacingNether")
    private boolean spawnersPreventPlacingNether = true;

    @Setting("combatlog.enabled")
    private boolean handleCombatLogging = true;

    @Setting("combatlog.despawnDelayTicks")
    private int combatlogDespawnDelayTicks = 600;

    @Setting("warzone.radiusOverworld")
    private int warzoneRadiusOverworld = 800;

    @Setting("warzone.radiusNether")
    private int warzoneRadiusNether = 800;

    @Setting("factions.conquest.pointLossPerDeath")
    private int conquestPointLossPerDeath = 20;

    @Setting("factions.conquest.requiredVictoryPoints")
    private int conquestRequiredVictoryPoints = 300;

    @Setting("factions.conquest.allowNegativePoints")
    private boolean conquestAllowNegativePoints = true;

    @Setting("factions.roads.allowClaimsBesides")
    private boolean allowClaimsBesidesRoads = true;

    //TODO: CaseInsensitiveList
    @Setting("factions.disallowedFactionNames")
    private List<String> factionDisallowedNames = new ArrayList<>();

    @Setting("factions.home.maxHeight")
    private int maxHeightFactionHome = -1;

    @Setting("factions.home.teleportDelay.NORMAL")
    private int factionHomeTeleportDelayOverworldSeconds;
    private long factionHomeTeleportDelayOverworldMillis;

    @Setting("factions.home.teleportDelay.NETHER")
    private int factionHomeTeleportDelayNetherSeconds;
    private long factionHomeTeleportDelayNetherMillis;

    @Setting("factions.home.teleportDelay.THE_END")
    private int factionHomeTeleportDelayEndSeconds;
    private long factionHomeTeleportDelayEndMillis;

    @Setting("factions.nameMinCharacters")
    private int factionNameMinCharacters = 3;

    @Setting("factions.nameMaxCharacters")
    private int factionNameMaxCharacters = 16;

    @Setting("factions.maxClaims")
    private int factionMaxClaims = 8;

    @Setting("factions.subclaim.nameMinCharacters")
    private int factionSubclaimNameMinCharacters = 3;

    @Setting("factions.subclaim.nameMaxCharacters")
    private int factionSubclaimNameMaxCharacters = 16;

    @Setting("factions.dtr.regenFreeze.baseMinutes")
    private int factionDtrRegenFreezeBaseMinutes = 40;
    private long factionDtrRegenFreezeBaseMilliseconds;

    @Getter(AccessLevel.NONE)
    @Setting("factions.dtr.regenFreeze.minutesPerMember")
    private int factionDtrRegenFreezeMinutesPerMember = 2;
    private long factionDtrRegenFreezeMillisecondsPerMember;

    @Setting("factions.dtr.minimum")
    private int factionMinimumDtr = -50;

    @Setting("factions.dtr.maximum")
    private float factionMaximumDtr = 6.0F;

    @Setting("factions.dtr.millisecondsBetweenUpdates")
    private int factionDtrUpdateMillis = 45000; // 45 seconds
    private String factionDtrUpdateTimeWords;

    @Setting("factions.dtr.incrementBetweenUpdates")
    private float factionDtrUpdateIncrement = 0.1F;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.warzone")
    private String relationColourWarzoneName = "LIGHT_PURPLE";
    private ChatColor relationColourWarzone = ChatColor.LIGHT_PURPLE;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.wilderness")
    private String relationColourWildernessName = "DARK_GREEN";
    private ChatColor relationColourWilderness = ChatColor.DARK_GREEN;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.teammate")
    private String relationColourTeammateName = "GREEN";
    private ChatColor relationColourTeammate = ChatColor.GREEN;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.ally")
    private String relationColourAllyName = "GOLD";
    private ChatColor relationColourAlly = ChatColor.GOLD;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.enemy")
    private String relationColourEnemyName = "RED";
    private ChatColor relationColourEnemy = ChatColor.RED;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.road")
    private String relationColourRoadName = "YELLOW";
    private ChatColor relationColourRoad = ChatColor.YELLOW;

    @Getter(AccessLevel.NONE)
    @Setting("factions.relationColours.safezone")
    private String relationColourSafezoneName = "AQUA";
    private ChatColor relationColourSafezone = ChatColor.AQUA;

    @Setter
    @Setting("deathban.baseDurationMinutes")
    private int deathbanBaseDurationMinutes = 60;

    @Setter
    @Setting("deathban.respawnScreenSecondsBeforeKick")
    private int deathbanRespawnScreenSecondsBeforeKick = 15;
    private long deathbanRespawnScreenTicksBeforeKick;

    @Setting("end.open")
    private boolean endOpen = true;

    @Setting("end.exitLocation")
    private String endExitLocationRaw = "world,0.5,75,0.5,0,0";
    private PersistableLocation endExitLocation = new PersistableLocation(Bukkit.getWorld("world"), 0.5, 75, 0.5);

    @Setting("end.extinguishFireOnExit")
    private boolean endExtinguishFireOnExit = true;

    @Setting("end.removeStrengthOnEntrance")
    private boolean endRemoveStrengthOnEntrance = true;

    @Setting("eotw.chatSymbolSuffix")
    private String eotwChatSymbolSuffix = "";

    //TODO: UUID list not UUID string list
    @Setting("eotw.lastMapCapperUuids")
    private List<String> eotwLastMapCapperUuids = new ArrayList<>();

    @Setting("subclaimSigns.private")
    private boolean subclaimSignPrivate = false;

    @Setting("subclaimSigns.captain")
    private boolean subclaimSignCaptain = false;

    @Setting("subclaimSigns.leader")
    private boolean subclaimSignLeader = false;

    @Setting("subclaimSigns.hopperCheck")
    private boolean subclaimHopperCheck = false;

    protected void updateFields() {
        factionDtrUpdateTimeWords = DurationFormatUtils.formatDurationWords(factionDtrUpdateMillis, true, true);
        relationColourWarzone = ChatColor.valueOf(relationColourWarzoneName.replace(" ", "_").toUpperCase());
        relationColourWilderness = ChatColor.valueOf(relationColourWildernessName.replace(" ", "_").toUpperCase());
        relationColourTeammate = ChatColor.valueOf(relationColourTeammateName.replace(" ", "_").toUpperCase());
        relationColourAlly = ChatColor.valueOf(relationColourAllyName.replace(" ", "_").toUpperCase());
        relationColourEnemy = ChatColor.valueOf(relationColourEnemyName.replace(" ", "_").toUpperCase());
        relationColourRoad = ChatColor.valueOf(relationColourRoadName.replace(" ", "_").toUpperCase());
        relationColourSafezone = ChatColor.valueOf(relationColourSafezoneName.replace(" ", "_").toUpperCase());
        factionDtrRegenFreezeBaseMilliseconds = TimeUnit.MINUTES.toMillis(factionDtrRegenFreezeBaseMinutes);
        factionDtrRegenFreezeMillisecondsPerMember = TimeUnit.MINUTES.toMillis(factionDtrRegenFreezeMinutesPerMember);
        factionHomeTeleportDelayOverworldMillis = TimeUnit.SECONDS.toMillis(factionHomeTeleportDelayOverworldSeconds);
        factionHomeTeleportDelayNetherMillis = TimeUnit.SECONDS.toMillis(factionHomeTeleportDelayNetherSeconds);
        factionHomeTeleportDelayEndMillis = TimeUnit.SECONDS.toMillis(factionHomeTeleportDelayEndSeconds);
        deathbanRespawnScreenTicksBeforeKick = TimeUnit.SECONDS.toMillis(deathbanRespawnScreenSecondsBeforeKick) / 50L;

        String[] split = endExitLocationRaw.split(",");
        if (split.length == 6) {
            try {
                String worldName = split[0];
                if (Bukkit.getWorld(worldName) != null) {
                    Integer x = Integer.parseInt(split[0]);
                    Integer y = Integer.parseInt(split[1]);
                    Integer z = Integer.parseInt(split[2]);
                    Float yaw = Float.parseFloat(split[3]);
                    Float pitch = Float.parseFloat(split[3]);

                    endExitLocation = new PersistableLocation(worldName, x, y, z);
                    endExitLocation.setYaw(yaw);
                    endExitLocation.setPitch(pitch);
                }
            } catch (NumberFormatException ignored) {
            }
        }

    }
}