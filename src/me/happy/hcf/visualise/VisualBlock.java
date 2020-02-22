package me.happy.hcf.visualise;

import org.bukkit.Location;

public class VisualBlock {

    private VisualType visualType;

    private VisualBlockData blockData;
    private Location location;

    public VisualBlock(VisualType visualType, VisualBlockData blockData, Location location) {
        this.visualType = visualType;
        this.blockData = blockData;
        this.location = location;
    }

    public VisualType getVisualType() {
        return visualType;
    }

    public VisualBlockData getBlockData() {
        return blockData;
    }

    public Location getLocation() {
        return location;
    }
}
