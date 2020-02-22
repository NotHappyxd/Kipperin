package me.happy.hcf.sotw;

import lombok.Getter;
import me.happy.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SotwTimer {

    @Getter
    private SotwRunnable sotwRunnable;

    @Getter
    private Set<UUID> enabled = new HashSet<>();

    public boolean cancel() {
        if (this.sotwRunnable != null) {
            this.sotwRunnable.cancel();
            this.sotwRunnable = null;
            enabled.clear();
            return true;
        }

        return false;
    }

    public void start(long millis) {
        if (this.sotwRunnable == null) {
            this.sotwRunnable = new SotwRunnable(this, millis);
            this.sotwRunnable.runTaskLater(HCF.getPlugin(), millis / 50L);
        }
    }

    public static class SotwRunnable extends BukkitRunnable {

        private SotwTimer sotwTimer;
        private long startMillis;
        private long endMillis;

        public SotwRunnable(SotwTimer sotwTimer, long duration) {
            this.sotwTimer = sotwTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
        }

        public long getRemaining() {
            return endMillis - System.currentTimeMillis();
        }

        @Override
        public void run() {
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "SOTW Protection is now over!");
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You are no longer invincible.");
            this.cancel();
            this.sotwTimer.sotwRunnable = null;
            this.sotwTimer.enabled.clear();
        }
    }
}
