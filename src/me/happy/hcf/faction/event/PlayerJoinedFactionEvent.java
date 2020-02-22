package me.happy.hcf.faction.event;

import lombok.Getter;
import me.happy.hcf.faction.type.Faction;
import me.happy.hcf.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Event called when a user has joined a {@link Faction}.
 */
public class PlayerJoinedFactionEvent extends FactionEvent {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final CommandSender sender;
    @Getter
    private final UUID playerUUID;
    private Optional<Player> player; // lazy-load

    public PlayerJoinedFactionEvent(CommandSender sender, @Nullable Player player, UUID playerUUID, PlayerFaction playerFaction) {
        super(playerFaction);

        Objects.requireNonNull(sender, "Sender cannot be null");
        Objects.requireNonNull(playerUUID, "Player UUID cannot be null");
        Objects.requireNonNull(playerFaction, "Player faction cannot be null");

        this.sender = sender;
        if (player != null) {
            this.player = Optional.of(player);
        }

        this.playerUUID = playerUUID;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the optional {@link Player} joining, this will load lazily.
     *
     * @return the {@link Player} or {@link Optional#empty()} ()} or if offline
     */
    public Optional<Player> getPlayer() {
        if (this.player == null) {
            this.player = Optional.ofNullable(Bukkit.getPlayer(this.playerUUID));
        }

        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public PlayerFaction getFaction() {
        return (PlayerFaction) super.getFaction();
    }
}