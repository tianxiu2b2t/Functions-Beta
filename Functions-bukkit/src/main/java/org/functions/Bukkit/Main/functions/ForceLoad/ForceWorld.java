package org.functions.Bukkit.Main.functions.ForceLoad;

import org.bukkit.World;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.Server.FWorld;

public class ForceWorld {
    FWorld fworld;
    World world;
    public ForceWorld(FWorld world) {
       this.fworld = world;
       this.world = fworld.getWorld();
    }
    public ForceWorld(World world) {
        fworld = Functions.instance.getFServer().getWorld(world.getUID());
        this.world = world;
    }
}
