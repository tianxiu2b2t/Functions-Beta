package org.functions.Bukkit.Main;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.functions.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager implements Runnable {
    static List<User> user = new ArrayList<>();
    Server server;
    public PlayerManager(Server server) {
        this.server = server;
    }
    public void run() {
        user.clear();
        for (Player p : server.getOnlinePlayers()) {
            user.add(new User(p.getUniqueId()));
        }
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
}
