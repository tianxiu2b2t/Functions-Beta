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
                if (temp.contains(".")) {
                    String[] t = temp.split("\\.");
                    //if (t[0].equalsIgnoreCase(pm[0])) {
                        //if (pm[1] != null) {
                            //if (t[1].startsWith("*")) {
                                //return true;
                            //}
                        //}
                    //}
                    for (int i = 0; i < pm.length; i++) {
                        if (t[i].equalsIgnoreCase(pm[i])) {
                            if (t[i+1].startsWith("*")) {
                                return true;
                            } else if (t[i + 1] != null || pm[i + 1] != null) {
                                if (t[i + 1].equalsIgnoreCase(pm[i + 1])) {
                                    return true;
                                }
                            }
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
                if (t[0].equalsIgnoreCase(Permission)) {
                    if (t[1].startsWith("*")) {
                        return true;
                    }
                }
            } else {
                if (temp.equalsIgnoreCase(Permission)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void sendMessagePermissions(Player player, String Permission) {
        player.sendMessage(Functions.instance.getAPI().noPermission(Permission));
    }
    public static boolean hasPermissionsSendMessage(Player player, String Permission) {
        if (!hasPermissions(player,Permission)) {
            sendMessagePermissions(player, Permission);
        }
        return true;
    }
}
