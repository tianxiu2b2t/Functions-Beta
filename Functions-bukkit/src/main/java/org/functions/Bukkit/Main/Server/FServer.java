package org.functions.Bukkit.Main.Server;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.functions.Bukkit.Main.Functions;

import java.io.*;
import java.net.URLDecoder;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FServer {
    List<FWorld> lw = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    LocalTime starts = LocalTime.now();
    //LocalTime starts = LocalTime.of(13,0);
    final long start = System.currentTimeMillis();
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
        long t = ChronoUnit.HOURS.between(starts,LocalTime.now());
        if (t < 0) {
            t = t + 24;
        }
        return t;
    }
    public long getServerStringForMinutes() {
        long t = ChronoUnit.MINUTES.between(starts,LocalTime.now()) % 60 * 60 / 60;
        if (t < 0) {
            t = t + 60;
        }
        return t;
    }
    public long getServerStringForSeconds() {
        long t = ChronoUnit.SECONDS.between(starts,LocalTime.now()) % 60;
        if (t < 0) {
            t = t + 60;
        }
        return t;
    }
    public long getServerStringForNanaSeconds() {
        long t = ChronoUnit.NANOS.between(starts,LocalTime.now());
        if (t < 0) {
            t = t + 1000;
        }
        return t;
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
    public List<Item> getItemStacks() {
        List<Item> ls = new ArrayList<>();
        server.getWorlds().forEach((w)->{
            getWorldItems(w).forEach(e->{
                ls.add((Item)e);
            });
        });
        return ls;
    }
    public boolean isBc() {
        File dir = Functions.instance.getFolder().getAbsoluteFile().getParentFile().getParentFile();
        File spigot = null;
        if (dir.listFiles()!=null) {
            for (File file : dir.listFiles()) {
                if (!file.getName().endsWith("spigot.yml")) {
                    continue;
                }
                spigot = file;
            }
        }
        if (spigot == null) {
            return false;
        }
        try {
            FileConfiguration fileConfiguration = new YamlConfiguration();
            fileConfiguration.load(spigot);
            return fileConfiguration.getBoolean("settings.bungeecord");
        } catch (IOException | InvalidConfigurationException e) {

        }
        return false;
    }
    public Functions getInstance() {
        return Functions.instance;
    }
    public int getEntitiesItemStack(World world) {
        return getWorldItems(world).stream().mapToInt(e -> ((Item) e).getItemStack().getAmount()).sum();
    }
    public int getEntitiesItemStacks() {
        return server.getWorlds().stream().mapToInt(this::getEntitiesItemStack).sum();
    }
    public void gc() {
        System.gc();
        Runtime.getRuntime().gc();
        if (Functions.instance.getConfiguration().getSettings().getBoolean("CancelAllPendingTasks",false)) {
            List<String> plugin_name = new ArrayList<>();
            Bukkit.getScheduler().getPendingTasks().forEach(e -> {
                plugin_name.add("Task(id): " + e.getTaskId() + ", by plugin: " + e.getOwner().getDescription().getName());
            });
            Bukkit.getScheduler().getPendingTasks().clear();
            plugin_name.forEach(Functions.instance::inLogs);
        }
        Functions.instance.inLogs("Chunk unload count: " + world_gc());
    }
    private int world_gc() {
        int i = 0;
        for (FWorld world : getWorlds()) {
            i = i + chunk_gc(world.world);
        }
        return i;
    }
    private int chunk_gc(World world) {
        int i = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            if (world.isChunkInUse(chunk.getX(),chunk.getZ())) {
                continue;
            }
            chunk.unload(true);
            i++;
        }
        return i;
    }
    public List<EntityType> getCanCleanTypes() {
        List<EntityType> ls = new ArrayList<>();
        if (getInstance().getConfiguration().getSettings().get("CanCleanType") != null) {
            getInstance().getConfiguration().getSettings().getStringList("CanCleanTypes").forEach(e->{
                String[] args = e.split(":");
                ls.add(EntityType.valueOf(args[0].toUpperCase()));
            });
        }
        return ls;
    }
    public void loadAllClass() {
        try {
            String jar = URLDecoder.decode(Functions.instance.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            ZipFile zip = new ZipFile(jar);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(jar));
            ZipInputStream zis = new ZipInputStream(bis);
            Enumeration<? extends ZipEntry> files = zip.entries();
            ZipEntry entry ;
            while ((entry = zis.getNextEntry())!=null) {
                String url = files.nextElement().getName().replace("\"", "/");
                if (url.toLowerCase().endsWith(".class")) {
                    try {
                        Class<?> clazz = Class.forName(url);
                    } catch (ClassNotFoundException e) {
                        Functions.instance.getClass().getClassLoader().loadClass(url);
                        Functions.instance.inLogs("load class successfully: " + url);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }
}
