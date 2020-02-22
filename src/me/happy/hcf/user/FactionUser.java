package me.happy.hcf.user;

import com.doctordark.util.GenericUtils;
import lombok.Getter;
import lombok.Setter;
import me.happy.hcf.deathban.Deathban;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class FactionUser implements ConfigurationSerializable {

    private final Set<UUID> factionChatSpying = new HashSet<>();
    private final UUID userUUID;

    private boolean capzoneEntryAlerts;
    private boolean showClaimMap;
    private boolean showLightning = true;
    private Deathban deathban;
    private long lastFactionLeaveMillis;
    private int kills;
    private int deaths;
    private Effect trail = null;

    public FactionUser(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public FactionUser(Map<String, Object> map) {
        this.factionChatSpying.addAll(GenericUtils.createList(map.get("faction-chat-spying"), String.class).stream().map(UUID::fromString).collect(Collectors.toList()));
        this.userUUID = UUID.fromString((String) map.get("userUUID"));
        this.capzoneEntryAlerts = (Boolean) map.get("capzoneEntryAlerts");
        //this.showClaimMap = (Boolean) map.get("showClaimMap");
        this.showLightning = (Boolean) map.get("showLightning");
        this.deathban = (Deathban) map.get("deathban");
        this.lastFactionLeaveMillis = Long.parseLong((String) map.get("lastFactionLeaveMillis"));
        this.kills = (Integer) map.get("kills");
        this.deaths = (Integer) map.get("deaths");
        if (map.containsKey("trail")) {
            trail = Effect.valueOf((String) map.get("trail"));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("faction-chat-spying", factionChatSpying.stream().map(UUID::toString).collect(Collectors.toList()));
        map.put("userUUID", userUUID.toString());
        map.put("capzoneEntryAlerts", capzoneEntryAlerts);
        map.put("showClaimMap", showClaimMap);
        map.put("showLightning", showLightning);
        map.put("deathban", deathban);
        map.put("lastFactionLeaveMillis", Long.toString(lastFactionLeaveMillis));
        map.put("kills", kills);
        map.put("deaths", deaths);
        if (trail != null) map.put("trail", trail.getName());
        return map;
    }

    public void removeDeathban() {
        deathban = null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(userUUID);
    }
}
