package org.functions.Bukkit.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.functions.Bukkit.Main.Functions;

public class WorldsEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void WorldUnload(WorldUnloadEvent event) {
        Functions.instance.getForceLoad().getForceChunksList().forEach((e->{
            if (e.world.equals(event.getWorld())) {
                event.setCancelled(true);
                //Functions.instance.inLogs("Unloading ForceWorldLoad: " + event.getWorld().getName() + " is load.");
            }
        }));
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void WorldChunkUnload(ChunkUnloadEvent event) {
        Functions.instance.getForceLoad().getForceChunksList().forEach((e->{
            if (e.world.equals(event.getWorld())) {
                if (e.chunk.equals(event.getChunk())) {
                    event.setCancelled(true);
                    e.chunk.getWorld().loadChunk(e.chunk);
                } else if (!e.chunk.isLoaded()) {
                    event.setCancelled(true);
                    e.chunk.getWorld().loadChunk(e.chunk);
                }
                //Functions.instance.inLogs("Unloading ForceChunkLoad: " + e.world.getName() + "," + e.chunk.getX() + "," + e.chunk.getZ());
            }
        }));
    }
}
