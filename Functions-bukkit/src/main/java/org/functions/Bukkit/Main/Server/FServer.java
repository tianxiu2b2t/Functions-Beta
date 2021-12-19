package org.functions.Bukkit.Main.Server;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

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
    //LocalTime starts = LocalTime.of(13,0);
    long start = System.currentTimeMillis();
    Server server;
    public FServer(Server server) {
        this.server = server;
        flushWorlds();
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
        return ChronoUnit.HOURS.between(starts,LocalTime.now()) % 24;
    }
    public long getServerStringForMinutes() {
        return ChronoUnit.MINUTES.between(starts,LocalTime.now()) % 60 * 60 / 60;
    }
    public long getServerStringForSeconds() {
        return ChronoUnit.SECONDS.between(starts,LocalTime.now()) % 60;
    }
    public long getServerStringForNanaSeconds() {
        return ChronoUnit.NANOS.between(starts,LocalTime.now());
    }
    public FWorld getWorld(UUID uuid) {
        flushWorlds();
        for (FWorld world : lw) {
            if (world.getWorld().getUID().equals(uuid)) {
                return world;
            }
        }
        return null;
    }
    public void clearWorlds() {
        lw.forEach(this::clearWorld);
    }
    public void clearWorld(FWorld world) {
        lw.remove(world);
    }
    public void flushWorlds() {
        if (lw.size() == 0) {
            for (World w : server.getWorlds()) {
                lw.add(new FWorld(w));
            }
            return;
        }
        lw.forEach((w)->{
            flushWorld(server.getWorld(w.world.getUID()));
        });
    }
    public void flushWorld(World world) {
        lw.forEach((w)->{
            if (w.getWorld().getUID().equals(world.getUID())) {
                w.world = world;
            }
        });
    }
    public List<FWorld> getWorlds() {
        return lw;
    }
    public boolean ReallyMemory = false;
    public void flushMemory() {
        Runtime runtime = Runtime.getRuntime();
//        long max = runtime.maxMemory();
//        long total = runtime.totalMemory();
//        long free = runtime.freeMemory();
//        if (free + total == max) {
//            ReallyMemory = true;
//        }
//        System.out.println(max);
//        System.out.println(free);
//        System.out.println(total);
//        System.out.println(total - free);
//        System.out.println(ReallyMemory);
        runtime.gc();
    }
    public int getCountEntities() {
        return getEntities().size();
    }
    public int getCountEntities(World world) {
        return getWorldEntities(world).size();
    }
    public int getCountItems() {
        return getItems().size();
    }
    public int getCountItems(World world) {
        return getWorldItems(world).size();
    }
    public List<Entity> getWorldEntities(World world) {
        return world.getEntities();
    }
    public List<Entity> getWorldItems(World world) {
        List<Entity> ls = new ArrayList<>();
        for (Entity e : world.getEntities()) {
            if (e.getType() == EntityType.DROPPED_ITEM) ls.add(e);
        }
        return ls;
    }
    public List<Entity> getEntities() {
        List<Entity> ls = new ArrayList<>();
        server.getWorlds().forEach((w)->{
            ls.addAll(getWorldEntities(w));
        });
        return ls;
    }
    public List<Entity> getItems() {
        List<Entity> ls = new ArrayList<>();
        server.getWorlds().forEach((w)->{
            ls.addAll(getWorldItems(w));
        });
        return ls;
    }
}
