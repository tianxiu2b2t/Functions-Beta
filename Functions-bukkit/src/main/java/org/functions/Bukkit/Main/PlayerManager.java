package org.functions.Bukkit.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager implements Runnable {
    static List<User> user = new ArrayList<>();
    static List<User> offlineuser = new ArrayList<>();
    static List<User> alluser = new ArrayList<>();
    static List<Group> groups = new ArrayList<>();
    Server server;
    public PlayerManager(Server server) {
        this.server = server;
    }
    public void run() {
        user.clear();
        for (Player p : server.getOnlinePlayers()) {
            user.add(new User(p.getUniqueId()));
        }
        offlineuser.clear();
        for (OfflinePlayer p : server.getOfflinePlayers()) {
            offlineuser.add(new User(p.getUniqueId()));
        }
        alluser.clear();
        alluser.addAll(user);
        alluser.addAll(offlineuser);
        groups.clear();
        Functions.instance.getConfiguration().group_Name.forEach((name)->{
            groups.add(new Group(Functions.instance.getConfiguration().groups.get(name)));
        });
    }
    public List<Group> getGroups() {
        return groups;
    }
    public List<User> getAllUser() {
        return alluser;
    }
    public Group getGroup(String name) {
        for (Group e : groups) {
            if (e.getGroupName().equalsIgnoreCase(name)) return e;
        }
        return null;
    }
    public List<User> getGroupUser(Group group) {
        List<User> t = user;
        for (User u : user) {
            if (u.getGroup() != group) t.remove(u);
        }
        return t;
    }
    public List<User> getUsers() {
        return user;
    }
    public User getUser(UUID uuid) {
        for (User u : user) {
            if (u.getPlayer().getUniqueId().toString().equals(uuid.toString())) {
                return u;
            }
        }
        return null;
    }
    public boolean exists(UUID uuid) {
        for (User u : user) {
            if (u.getPlayer().getUniqueId().toString().equals(uuid.toString())) {
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
}
