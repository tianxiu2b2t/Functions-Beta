package org.functions.Bukkit.Main.Server;

import org.bukkit.Server;
import org.bukkit.World;
import org.functions.Bukkit.Main.Functions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FWorld {
    World world;
    public FWorld(World world) {
        this.world = world;
    }
    public World getWorld() {
        return world;
    }
    public boolean between(long var, long low, long high) {
        return var >= low && var <= high;
    }

    public String getTagForTime() {
        long time = world.getTime();
        if (between(time, 13000L, 24000L)) {
            return "night";
        } else if (between(time, 12000L, 12999L)) {
            return "dusk";
        } else {
            return between(time, 0L, 999L) ? "dawn" : "day";
        }
    }
    public String getWorldStringForTime() {
        long time = world.getTime();
        // 1200 * 20
        // 1200
        //  20
        // 24000
        time = (time / 20 * 1200 + 60000);
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(Functions.instance.getConfiguration().getSettings().getString("Date_GameTime","HH:ss"));
        String[] space = sdf.format(date).split(Functions.instance.getConfiguration().getSettings().getString("Date_GameTime_split",":"));
        int h = Integer.parseInt(space[0]);
        int m = Integer.parseInt(space[1]);
        String s = "none";
        if (h > 23) {
            h = h - 24;
            s = h + Functions.instance.getConfiguration().getSettings().getString("Date_GameTime_split",":") + m;
        }
        return s;

    }
}
