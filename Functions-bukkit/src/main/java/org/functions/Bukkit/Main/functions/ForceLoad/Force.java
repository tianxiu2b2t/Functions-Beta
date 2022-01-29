package org.functions.Bukkit.Main.functions.ForceLoad;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.Server.FServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Force {
    FServer server;
    ForceFileChunk forceFileChunk;
    ForceFileWorld forceFileWorld;
    ForceChunk[] chunks;
    ForceWorld[] worlds;
    public Force(FServer server) {
        this.server = server;
        forceFileChunk = new ForceFileChunk(Functions.instance.getDirPath("ForceChunkFiles"));
        chunks = readForceChunk();
        forceFileWorld = new ForceFileWorld(Functions.instance.getDirPath("ForceWorldFiles"));
        worlds = readForceWorld();
    }
    public ForceChunk[] getForceChunks() {
        return chunks;
    }
    public List<ForceChunk> getForceChunksList() {
        return changeForceChunksToForceChunksList(chunks);
    }
    public ForceChunk[] changeForceChunksListToForceChunks(List<ForceChunk> chunks) {
        ForceChunk[] chunk = new ForceChunk[chunks.size()];
        for (int i = 0; i < chunks.size(); i++) {
            chunk[i] = chunks.get(i);
        }
        return chunk;
    }
    public List<ForceChunk> changeForceChunksToForceChunksList(ForceChunk[] chunks) {
        return new ArrayList<>(Arrays.asList(chunks));
    }
    public ForceWorld[] getForceWorlds() {
        return worlds;
    }
    public List<ForceWorld> getForceWorldsList() {
        return changeForceWorldsToForceWorldsList(worlds);
    }
    public ForceWorld[] changeForceWorldsListToForceWorlds(List<ForceWorld> worlds) {
        ForceWorld[] world = new ForceWorld[worlds.size()];
        for (int i = 0; i < worlds.size(); i++) {
            world[i] = worlds.get(i);
        }
        return world;
    }
    public List<ForceWorld> changeForceWorldsToForceWorldsList(ForceWorld[] worlds) {
        return new ArrayList<>(Arrays.asList(worlds));
    }
    public void run() {
        changeForceChunksToForceChunksList(chunks).forEach((e)-> {
            e.world.loadChunk(e.chunk);
        });
        saveChunk();
        saveWorld();
    }
    public void add(Chunk chunk) {
        ForceChunk forceChunk = new ForceChunk(chunk);
        int i = chunks.length + 1;
        ForceChunk[] temp = new ForceChunk[i];
        for (int s = 0; s < i; s++) {
            if (chunks.length == s) {
                temp[s] = forceChunk;
                continue;
            }
            temp[s] = chunks[s];
        }
        chunks = temp;
    }
    public void remove(Chunk chunk) {
        int i = chunks.length - 1;
        ForceChunk[] temp = new ForceChunk[i];
        int f = 0;
        for (int s = 0; s < i; s++) {
            if (!chunks[s].chunk.equals(chunk)) {
                temp[s] = chunks[s - f];
            }
            f++;
        }
        chunks = temp;
    }
    public void saveChunk() {
        forceFileChunk.saveChunks(chunks);
    }
    public ForceChunk[] readForceChunk() {
        Functions.instance.print("Successfully read ForceChunks");
        return forceFileChunk.readFiles();
    }
    public void add(World world) {
        ForceWorld forceWorld = new ForceWorld(world);
        int i = worlds.length + 1;
        ForceWorld[] temp = new ForceWorld[i];
        for (int s = 0; s < i; s++) {
            if (worlds.length == s) {
                temp[s] = forceWorld;
                continue;
            }
            temp[s] = worlds[s];
        }
        worlds = temp;
    }
    public void remove(World world) {
        int i = worlds.length - 1;
        ForceWorld[] temp = new ForceWorld[i];
        int f = 0;
        for (int s = 0; s < i; s++) {
            if (!worlds[s].world.equals(world)) {
                temp[s] = worlds[s - f];
            }
            f++;
        }
        worlds = temp;
    }
    public void saveWorld() {
        forceFileChunk.saveChunks(chunks);
    }
    public ForceWorld[] readForceWorld() {
        Functions.instance.print("Successfully read ForceWorlds");
        return forceFileWorld.readFiles();
    }
}
