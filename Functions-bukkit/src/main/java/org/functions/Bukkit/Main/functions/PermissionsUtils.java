package org.functions.Bukkit.Main.functions;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.PlayerManager;

public class PermissionsUtils {
    //这里写检查是否有权限
    @SuppressWarnings("all")
    public static boolean hasPermissions(Player player, String Permission) {
        if (Permission.contains(".")) {
            String[] pm = Permission.split("\\.");
            for (String temp : Functions.instance.getPlayerManager().getUser(player.getUniqueId()).getPermissions()) {
                if (temp.startsWith("*")) {
                    return true;
                }
                String[] t = temp.split("\\.");
                for (int i = 1; i > pm.length; i++) {
                    if (t[i - 1].equalsIgnoreCase(pm[i - 1])) {
                        if (t[i - 1].startsWith("*")) {
                            return true;
                        } else if (t[i].equalsIgnoreCase(pm[i])) {
                            return true;
                        }
                    }
                }
            }
        }
        for (String temp : Functions.instance.getPlayerManager().getUser(player.getUniqueId()).getPermissions()) {
            if (temp.startsWith("*")) {
                return true;
            }
            if (temp.contains(".")) {
                String[] t = temp.split("\\.");
                if (t[1].startsWith("*")) {

                }
            }
        }
        return false;
    }
}
