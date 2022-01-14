package org.functions.Bukkit.Main.functions;

import org.bukkit.Location;
import org.functions.Bukkit.Main.Functions;

import java.util.UUID;

public class Back {
    static YamlUsers yamlUsers = Functions.instance.yamlUsers();
    public static boolean isDeath(UUID uuid) {
        return yamlUsers.configurations.get(uuid).get("Death")!=null;
    }
    public static void setDeathPos(UUID uuid, Location pos) {
        if (pos==null) yamlUsers.set(uuid,"Death",null);
        if (pos!=null) yamlUsers.set(uuid,"Death",Functions.instance.getAPI().changeLocationToString(pos));
    }
    public static void setDeath(UUID uuid, Location pos) {
        setDeathPos(uuid,pos);
    }
    public static Location getDeathPos(UUID uuid) {
        if (isDeath(uuid)) return Functions.instance.getAPI().changeStringToLocation(getDeathPosString(uuid));
        return Functions.instance.getServer().getPlayer(uuid).getBedSpawnLocation();
    }
    public static String getDeathPosString(UUID uuid) {
        return yamlUsers.configurations.get(uuid).getString("Death");
    }
}
