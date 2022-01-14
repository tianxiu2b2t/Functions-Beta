package org.functions.Bukkit.Main.functions;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.util.LinkedHashMap;
import java.util.UUID;

public class TeleportA {
    static LinkedHashMap<UUID, Long> cache = new LinkedHashMap<>();
    static LinkedHashMap<UUID, Player> cachePlayer = new LinkedHashMap<>();
    public static boolean isDelay(UUID uuid) {
        if (isInCache(uuid)) return (cache.get(uuid) + Functions.instance.getConfiguration().getSettings().getInt("TpaDelay",10) * 1000L) < System.currentTimeMillis();
        return false;
    }
    public static boolean isInCache(UUID uuid) {
        return cache.get(uuid)!=null;
    }
    public static boolean putInCache(UUID uuid) {
        if (!isInCache(uuid)) {
            cache.put(uuid,System.currentTimeMillis());
            return true;
        }
        return false;
    }
    public static boolean removeInCache(UUID uuid) {
        if (isInCache(uuid)) {
            cache.remove(uuid);
            return true;
        }
        return false;
    }
    public static void autoClearCaches() {
        cache.forEach((uuid,e)->{
            if (isDelay(uuid)) {
                removeInCache(uuid);
            }
        });
    }
    public static void clearCache() {
        cache.clear();
    }
    // tpa -> player teleport to target.
    // tpahere -> target teleport player.
    public static boolean tp(boolean tpa,UUID player,UUID target) {
        if (!isInCache(player) || !isDelay(player)) {
            return false;
        }
        User player_user = Functions.instance.getPlayerManager().getUser(player);
        User target_user = Functions.instance.getPlayerManager().getUser(target);
        if (!player_user.getOfflinePlayer().isOnline() || !target_user.getOfflinePlayer().isOnline()) {
            return false;
        }
        if (tpa) {
            player_user.getPlayer().teleport(target_user.getPlayer().getLocation());
        } else {
            target_user.getPlayer().teleport(player_user.getPlayer().getLocation());
        }
        return true;
    }
}
