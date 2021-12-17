package org.functions.Bukkit.Main.Server;

import org.bukkit.Server;
import org.bukkit.World;
import org.functions.Bukkit.Main.Functions;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
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
        int time = Integer.parseInt((world.getTime() % 24000L)+"");
        return LocalTime.of(time / 1000, time % 1000 * 60 / 1000).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG));
    }
    public String getWorldStringForDayTime() {
        return ((int)(world.getFullTime() / 24000L % 2147483647L) + "");
    }
}
