package org.functions.Bukkit.API;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.functions.Bukkit.Main.Functions;

public class WorldBlock {
    int x = 0;
    int y = 0;
    int z = 0;
    Location loc;
    Block block;
    public WorldBlock(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        block = world.getBlockAt(x,y,z);
    }
    public WorldBlock(Block block) {
        this(block.getX(),block.getY(),block.getZ(),block.getWorld());
    }
    int status = 0;
    public int destroy() {
        block.breakNaturally();
        int t = 0;
        status++;
        t = status;
        status = 0;
        return t;
    }
    public int QuietDestroy() {
        return set(Material.AIR);
    }
    public int set(Material material) {
        block.setType(material,true);
        int t = 0;
        status++;
        t = status;
        status = 0;
        return t;
    }

    public Block getBlock() {
        return block;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location getLoc() {
        return loc;
    }
}
