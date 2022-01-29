package org.functions.Bukkit.Main.functions.WorldBlockStorage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.functions.Bukkit.API.WorldBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class StorageRead extends StorageMain {
    File dir;
    public StorageRead(File dir) {
        super(dir);
        this.dir = dir;
    }
    private List<Storage> getFiles() {
        List<Storage> files = new ArrayList<>();
        if (isDirAnyFiles()) {
            Arrays.asList(getDirFiles()).forEach(e->{
                LinkedHashMap<String,String> link = getInfo(e);
                files.add(new Storage(null,e,link.get("name"),getWorldBlocks(null,link.get("PositionAndBlocks")),link));
            });
        }
        return files;
    }
    public List<Storage> getFiles(Player player) {
        if (player==null) return getFiles();
        List<Storage> files = new ArrayList<>();
        if (isDirAnyFiles()) {
            Arrays.asList(getDirFiles()).forEach(e->{
                LinkedHashMap<String,String> link = getInfo(e);
                files.add(new Storage(player,e,link.get("name"),getWorldBlocks(player,link.get("PositionAndBlocks")),link));
            });
        }
        return files;
    }
    public LinkedHashMap<String,String> getInfo(File file) {
        LinkedHashMap<String,String> link = new LinkedHashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String input;
            while ((input = reader.readLine()) != null) {
                if (input.startsWith("id:")) {
                    link.put("id",getKey(input));
                }
                if (input.startsWith("name:")) {
                    link.put("name",getKey(input));
                }
                if (input.startsWith("initPosition:")) {
                    link.put("initPosition",getKey(input));
                }
                if (input.startsWith("PositionAndBlocks:")) {
                    link.put("PositionAndBlocks",getKey(input));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return link;
    }
    public List<WorldBlock> getWorldBlocks(Player player, String PositionAndBlock) {
        List<WorldBlock> worldBlockList = new ArrayList<>();
        List<String> ls = Arrays.asList(PositionAndBlock.split("|"));
        ls.forEach(e->{
            String[] args = e.split(",");
            List<String> lf = Arrays.asList(args);
            lf.remove(lf.size()-1);
            lf.remove(lf.size()-1);
            WorldBlock worldBlock = new WorldBlock(getLocation(player,lf));
            worldBlock.setInCacheMaterial(getMaterial(args[args.length - 2]));
            worldBlockList.add(worldBlock);
        });
        return worldBlockList;
    }
    public String getKeyLaterKey(String key,String params) {
        return params.split(key)[1];
    }
    public Material getMaterial(String name) {
        for (Material e : Material.values()) {
            if (e.name().equalsIgnoreCase(name)){
                return e;
            }
            if (e.name().contains(name)) {
                return e;
            }
        }
        return null;
    }
    public Location getLocation(Player player, List<String> position) {
        World world = Bukkit.getWorlds().get(0);
        List<Integer> ld = new ArrayList<>();
        position.forEach(e->{
            if (e.toLowerCase().startsWith("~")) {
                ld.add(Integer.parseInt(getKeyLaterKey("~",e)));
            }
        });
        if (player!=null) {
            world = player.getWorld();
            return new Location(world,player.getLocation().getBlock().getX() + ld.get(0),player.getLocation().getBlock().getY() + ld.get(1),player.getLocation().getZ() + ld.get(2));
        }
        return new Location(world,ld.get(0),ld.get(1),ld.get(2));
    }
    public String getKey(String input) {
        return input.split(":")[1];
    }
    public List<String> ListAddKeyReturnList(String input) {
        return ListAddReturnList(getKey(input));
    }
    public List<String> getList(String input) {
        return ListAddKeyReturnList(input);
    }
    public List<String> ListAddReturnList(String input) {
        List<String> ls = new ArrayList<>();
        ls.add(input);
        return ls;
    }
    public boolean isDirAnyFiles() {
        if (dir.exists()) {
            if (dir.listFiles()!=null) {
                return true;
            }
        } else {
            dir.mkdirs();
        }
        return false;
    }
    public File[] getDirFiles() {
        if (isDirAnyFiles()) {
            return dir.listFiles();
        }
        return null;
    }
}
