package org.functions.Bukkit.Main.functions;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.functions.Bukkit.Main.Functions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;

public class YamlUsers {
    public File folder = new File(Functions.instance.getDataFolder().getAbsolutePath().replace('/','\\') + "/Users");
    public LinkedHashMap<UUID, FileConfiguration> configurations;
    public LinkedHashMap<UUID, File> file;
    public YamlUsers(File folder) {
        configurations = new LinkedHashMap<>();
        file = new LinkedHashMap<>();
        this.folder = folder;
        folder.mkdirs();
        getAllUserFile();
        initUsersFileConfiguration();
    }
    public void initUserFileConfiguration(UUID uuid) {
        File user = new File(folder,uuid.toString()+".yml");
        createUser(uuid);
        getAllUserFile();
        if (configurations.get(uuid)==null) configurations.put(uuid,YamlConfiguration.loadConfiguration(user));
    }
    public void initUsersFileConfiguration() {
        if (Functions.instance.getServer().getOfflinePlayers().length == 0) {
            return;
        }
        Arrays.asList(Functions.instance.getServer().getOfflinePlayers()).forEach((e)->{initUserFileConfiguration(e.getUniqueId());});
    }
    public void saveUserFileConfiguration(UUID uuid) {
        if (configurations.get(uuid)==null) {
            Preconditions.checkArgument(configurations.get(uuid)!=null,"The player is cheat join the server?");
        }
        try {
            configurations.get(uuid).save(file.get(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveUsersFileConfiguration() {
        Arrays.asList(Functions.instance.getServer().getOfflinePlayers()).forEach((e)->{saveUserFileConfiguration(e.getUniqueId());});
    }
    public void getAllUserFile() {
        if (!folder.exists()) folder.mkdirs();
        File[] files = folder.listFiles();
        if (files!=null) {
            Arrays.asList(files).forEach(e->{
                UUID uuid = UUID.fromString(e.getName().replace(".yml",""));
                file.putIfAbsent(uuid, e);
            });
        }
    }
    public void set(UUID uuid,String path,Object value) {
        configurations.get(uuid).set(path, value);
        saveUserFileConfiguration(uuid);
    }
    public boolean exists(UUID uuid) {
        return file.get(uuid) != null && configurations.get(uuid) != null;
    }
    public void createUser(UUID uuid) {
        File user = new File(folder,uuid.toString()+".yml");
        if (!user.exists()) {
            try {
                user.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
