package org.functions.Bukkit.Main.Server;

import org.bukkit.Server;
import org.bukkit.World;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FServer {
    List<FWorld> lw = new ArrayList<>();
    LocalTime starts = LocalTime.now();
    long start = System.currentTimeMillis();
    Server server;
    public FServer(Server server) {
        this.server = server;
        for (World w : server.getWorlds()) {
            lw.add(new FWorld(w));
        }
    }

    public Server getServer() {
        return server;
    }
    public long getServerTime() {
        return System.currentTimeMillis() - start;
    }
    public String getServerStringForTime() {
        int day = (int)getServerTime() / (1000 * 24 * 60 * 60);
        return day + " " + ChronoUnit.HOURS.between(starts,LocalTime.now()) + ":" + ChronoUnit.MINUTES.between(starts,LocalTime.now()) + ":" + ChronoUnit.SECONDS.between(starts,LocalTime.now());
    }
    public long getServerStringForDays() {
        return (int)getServerTime() / (1000 * 24 * 60 * 60);
    }
    public long getServerStringForHours() {
        return ChronoUnit.HOURS.between(starts,LocalTime.now());
    }
    public long getServerStringForMinutes() {
        return ChronoUnit.MINUTES.between(starts,LocalTime.now());
    }
    public long getServerStringForSeconds() {
        return ChronoUnit.SECONDS.between(starts,LocalTime.now());
    }
    public long getServerStringForNanaSeconds() {
        return ChronoUnit.NANOS.between(starts,LocalTime.now());
    }
    public FWorld getWorld(UUID uuid) {
        for (FWorld world : lw) {
            if (world.getWorld().getUID().equals(uuid)) {
                return world;
            }
        }
        return null;
    }
    public List<FWorld> getWorlds() {
        return lw;
    }
}
