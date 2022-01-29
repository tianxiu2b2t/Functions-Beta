package org.functions.Bukkit.Main.functions.ForceLoad;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.functions.Bukkit.Main.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ForceFileWorld {
    File dir;
    public ForceFileWorld(File dir) {
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
    public World readStringToWorld(String params) {
        if (params.startsWith("world:")) {
            return Bukkit.getWorld(params.replace("world:",""));
        }
        return null;
    }

    public ForceWorld[] readFiles() {
        ForceWorld[] worlds;
        List<World> worldsList = new ArrayList<>();
        if (isDirExistsFiles()) {
            BufferedReader reader;
            for (File file : getDirForceFileEndsWithFiles()) {
                World world = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String input;
                    while ((input = reader.readLine())!=null) {
                        world = readStringToWorld(input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (world != null) {
                    worldsList.add(world);
                }
            }
            worlds = new ForceWorld[worldsList.size()];
            for (int i = 0; i < worldsList.size(); i++) {
                worlds[i] = new ForceWorld(worldsList.get(i));
                ForceWorld world = worlds[i];
                Functions.instance.print("Successfully load forceWorld Name: " + world.world.getName());
            }
            return worlds;
        }
        return null;
    }
    public void saveWorlds(ForceWorld[] forceChunks) {
        for (ForceWorld world : forceChunks) {
            String name = world.world.getName();
            File file = new File(dir, name + ".forceFile");
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write("world:" + name);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Functions.instance.print("Successfully save forceWorld: " + file.getName());
        }
    }
}
