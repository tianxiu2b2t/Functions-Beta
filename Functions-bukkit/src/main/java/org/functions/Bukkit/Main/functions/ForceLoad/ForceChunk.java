package org.functions.Bukkit.Main.functions.ForceLoad;

import org.bukkit.Chunk;
import org.bukkit.World;

public class ForceChunk extends ForceWorld {
    public Chunk chunk;
    public World world;
    public ForceChunk(Chunk chunk) {
        super(chunk.getWorld());
        world = chunk.getWorld();
        this.chunk = chunk;
    }
}
