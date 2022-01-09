package org.functions.Bukkit.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.User;
import org.functions.Bukkit.Main.functions.YamlUsers;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class PlayerManager implements Runnable {
    YamlUsers yamlUsers;
    public LinkedHashMap<UUID, FileConfiguration> configurations = new LinkedHashMap<>();
    public LinkedHashMap<UUID, File> file = new LinkedHashMap<>();
    static List<User> user = new ArrayList<>();
    static List<User> offlineuser = new ArrayList<>();
    static List<User> alluser = new ArrayList<>();
    static List<Group> groups = new ArrayList<>();
    Server server;
    public PlayerManager(Server server) {
        this.server = server;
        configurations = new LinkedHashMap<>();
        file = new LinkedHashMap<>();
        run();
    }
    public void run() {
        for (OfflinePlayer p : server.getOfflinePlayers()) {
            if (alluser.size() == 0) {
                alluser.add(new User(p.getUniqueId()));
            }
            if (!getUserIsInList(p.getUniqueId())) {
                alluser.add(new User(p.getUniqueId()));
            }
        }
        groups.clear();
        Functions.instance.getConfiguration().group_Name.forEach((name)->{
            groups.add(new Group(Functions.instance.getConfiguration().groups.get(name)));
        });
    }
    public List<Group> getGroups() {
        run();
        return groups;
    }
    public List<User> getAllUser() {
        run();
        return alluser;
    }
    public Group getGroup(String name) {
        run();
        for (Group e : groups) {
            if (e.getGroupName().equalsIgnoreCase(name)) return e;
        }
        return null;
    }
    public List<User> getGroupUser(Group group) {
        run();
        List<User> t = alluser;
        for (User u : alluser) {
            if (u.getGroup() != group) t.remove(u);
        }
        return t;
    }
    public boolean getUserIsInList(UUID uuid) {
        for (User user : alluser) {
            if (user.getOfflinePlayer().getUniqueId().toString().equals(uuid.toString())) {
                return true;
            }
        }
        return false;
    }
    public List<User> getUsers() {
        run();
        return alluser;
    }
    public User getUser(UUID uuid) {
        run();
        for (User u : alluser) {
            if (u.getOfflinePlayer().getUniqueId().toString().equals(uuid.toString())) {
                return u;
            }
        }
        return getUser(uuid);
    }
    public boolean exists(UUID uuid) {
        run();
        for (User u : alluser) {
            if (u.getOfflinePlayer().getUniqueId().toString().equals(uuid.toString())) {
                return true;
            }
        }
        return false;
    }
    public User getUser(String name) {
        run();
        for (User u : alluser) {
            if (u.getOfflinePlayer().getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return u;
            }
        }
        return getUser(name);
    }
    public User getUser(String name,boolean exists) {
        if (exists) {
            run();
            for (User u : alluser) {
                if (u.getOfflinePlayer().getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                    return u;
                }
            }
            return null;
        }
        return getUser(name);
    }
    public boolean exists(String name) {
        run();
        for (User u : alluser) {
            if (u.getOfflinePlayer().getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    public void sendAllTellRaw(String text) {
        getUsers().forEach((u)->{u.sendTellRaw(text);});
    }
    @Deprecated
    public void sendParseAllTellRaw(String text) {
        getUsers().forEach((u)->{u.sendParseTellRaw(text);});
    }
    public String NotFound(String name) {
        return Functions.instance.getAPI().putLanguage("NotFoundUser","&cm没有找到 %player% 用户!",null,new Object[]{"player",name});
    }
}
