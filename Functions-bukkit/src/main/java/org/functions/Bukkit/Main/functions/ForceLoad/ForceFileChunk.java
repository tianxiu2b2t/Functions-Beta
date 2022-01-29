package org.functions.Bukkit.Main.functions.ForceLoad;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.functions.Bukkit.Main.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForceFileChunk {
    File dir;
    public ForceFileChunk(File dir) {
        this.dir = dir;
    }
    public void mkdirs() {
        dir.mkdirs();
    }
    public boolean isDirExistsFiles() {
        if (dir.exists()) {
            return dir.listFiles() != null;
        }
        mkdirs();
        return false;
    }
    public File[] getDirFiles() {
        if (isDirExistsFiles()) {
            return dir.listFiles();
        }
        return null;
    }
    public File[] getDirForceFileEndsWithFiles() {
        int length = 0;
        List<File> filesList = new ArrayList<>();
        if (isDirExistsFiles()) {
            for (File file : getDirFiles()) {
                if (!file.getName().endsWith(".forceFile")) {
                    continue;
                }
                length++;
                filesList.add(file);
            }
            File[] files = new File[length];
            for (int i = 0; i < length; i++) {
                files[i] = filesList.get(i);
            }
            return files;
        }
        return null;
    }
    public Location readStringToLocation(String params) {
        if (params.startsWith("position:")) {
            return Functions.instance.getAPI().changeStringToLocation(params.replace("position:",""));
        }
        return null;
    }

    public ForceChunk[] readFiles() {
        ForceChunk[] chunks;
        List<Location> positions = new ArrayList<>();
        if (isDirExistsFiles()) {
            BufferedReader reader;
            for (File file : getDirForceFileEndsWithFiles()) {
                Location position = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String input;
                    while ((input = reader.readLine())!=null) {
                        position = readStringToLocation(input);
                        if (WorldIsNull(position.getWorld())) {
                            position = null;
                            Functions.instance.print("Failed load forceChunk file: " + file.getName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (position != null) {
                    positions.add(position);
                }
            }
            chunks = new ForceChunk[positions.size()];
            for (int i = 0; i < positions.size(); i++) {
                chunks[i] = new ForceChunk(positions.get(i).getChunk());
                ForceChunk chunk = chunks[i];
                Functions.instance.print("Successfully load forceChunk Position: " + Functions.instance.getAPI().changeLocationToString(new Location(chunk.world,chunk.chunk.getX(),0,chunk.chunk.getZ())));
            }
            return chunks;
        }
        return null;
    }
    public void saveChunks(ForceChunk[] forceChunks) {
        for (ForceChunk chunk : forceChunks) {
            Location pos = new Location(chunk.world, chunk.chunk.getX(), 0, chunk.chunk.getZ());
            File file = new File(dir, chunk.world.getName() + "," + chunk.chunk.getX() + "," + chunk.chunk.getZ() + ".forceFile");
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write("position:" + Functions.instance.getAPI().changeLocationToString(pos));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Functions.instance.print("Successfully save forceChunk: " + file.getName());
        }
    }
    public boolean WorldIsNull(World world) {
        if (world == null) return true;
        for (World world1 : Bukkit.getWorlds()) {
            if (world1.getUID().equals(world.getUID())) {
                return false;
            }
        }
        return true;
    }
}
