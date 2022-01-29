package org.functions.Bukkit.Main.functions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.PlayerManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PermissionsUtils {
    //这里写检查是否有权限
    @SuppressWarnings("all")
    public static boolean hasPermissions(Player player, String perm) {
        for (String s : getPermissions(player)) {
            if (s.equalsIgnoreCase("*")) {
                return true;
            }
            if (s.contains(".")) {
                String[] st = s.split("\\.");
                if (perm.contains(".")) {
                    String[] permt = perm.split("\\.");
                    int length = st.length;
                    for (int i = 0; i < length; i++) {
                        if (!st[i].equalsIgnoreCase(permt[i])) {
                            i = length + 1;
                            continue;
                        }
                        if (i != 0) {
                            if (st[i - 1].equalsIgnoreCase(permt[i - 1])) {
                                if (st[i].equalsIgnoreCase("*")) {
                                    return true;
                                }
                            }
                        }
                    }
                } else {
                    if (perm.equalsIgnoreCase(s)) {
                        return true;
                    } else if (perm.equalsIgnoreCase(st[0])) {
                        return true;
                    }
                }
            } else {
                if (perm.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static List<String> getPermissions(Player player) {
        return Functions.instance.getPlayerManager().getUser(player.getUniqueId()).getPermissions();
    }
    @Deprecated
    /**
     *
     * @param player Player can is null or Minecraft get Player.
     * @param Permission player has permission? need if true or false.
     * @deprecated if error.
     * @Return return user has permissions.
     */
    private boolean hasPermissionsOld(Player player, String Permission) {
        if (player==null) {
            return true;
        }
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
                        if (!t[0].equalsIgnoreCase(pm[0])) {
                            continue;
                        }
                        if (t.length == i || pm.length == i) {
                            if (t[i - 1].equalsIgnoreCase(pm[i - 1])) {
                                if (t[i].startsWith("*")) {
                                    return true;
                                }
                            }
                        }
                        int ti = i - 1;
                        // 3 <= 2
                        if (t.length <= i) {
                            continue;
                        }
                        if (!t[i].equalsIgnoreCase(pm[i])) {
                            continue;
                        }
                        if (t.length >= ti) {
                            if (t[i].equalsIgnoreCase(pm[i])) {
                                if (t.length == i) {
                                    return true;
                                }
                                int ts = i + 1;
                                // t .length = 5;
                                // 5 < 6
                                if (t.length == ts) {
                                    if (t[ts] != null) {
                                        if (t[ts].startsWith("*")) {
                                            return true;
                                        }
                                    }
                                } else if (t[i] != null || pm[i] != null) {
                                    if (t[i].equalsIgnoreCase(pm[i])) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (String temp : Functions.instance.getPlayerManager().getUser(player.getUniqueId()).getPermissions()) {
            System.out.println(temp);
            if (temp.startsWith("*")) {
                return true;
            }
            if (temp.contains(".")) {
                String[] t = temp.split("\\.");
                if (t[0].equalsIgnoreCase(Permission)) {
                    if (t.length == 1) {
                        if (t[1].startsWith("*")) {
                            return true;
                        }
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
    public static boolean hasPermissionsSendMessage(CommandSender player, String Permission) {
        if (!(player instanceof Player)) {
            return true;
        }
        if (!hasPermissions((Player) player,Permission)) {
            sendMessagePermissions((Player) player, Permission);
            return false;
        }
        return true;
    }
    public static boolean hasPermissionsSendMessage(CommandSender player, String Permission,boolean Tab) {
        if (Tab) {
            return !hasPermissionsSendMessage(player, Permission);
        }
        return hasPermissionsSendMessage(player, Permission);
    }
    public static boolean hasPermissionsSendMessage(Player player, String Permission) {
        if (!hasPermissions(player,Permission)) {
            sendMessagePermissions(player, Permission);
            return false;
        }
        return true;
    }
    public static LinkedHashMap<String,PermissionAttachment> attachments = new LinkedHashMap<>();
    public static LinkedHashMap<UUID,PermissionAttachment> loadPermissionAttachment = new LinkedHashMap<>();
    public static LinkedHashMap<String, Permission> registeredPermission = new LinkedHashMap<>();
    public static void updatePlayerCommand(Player player) {
        boolean hasCommands = false;
        try {
            Player.class.getMethod("updateCommands");
            hasCommands = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (hasCommands) {
            try {
                player.getClass().getDeclaredMethod("updateCommands").invoke(player);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void collectPermissions() {
        registeredPermission.clear();
        Bukkit.getPluginManager().getPermissions().forEach((p)->{
            registeredPermission.put(p.getName().toLowerCase(),p);
        });
    }
    public static Map<String, Boolean> getChildren(String node) {

        Permission perm = registeredPermission.get(node.toLowerCase());
        if (perm == null)
            return null;

        return perm.getChildren();

    }
    public static Map<String, Boolean> getAllChildren(String node, Set<String> playerPermArray) {

        LinkedList<String> stack = new LinkedList<>();
        Map<String, Boolean> alreadyVisited = new HashMap<>();
        stack.push(node);
        alreadyVisited.put(node, true);

        while (!stack.isEmpty()) {
            String now = stack.pop();

            Map<String, Boolean> children = getChildren(now);

            if ((children != null) && (!playerPermArray.contains("-" + now))) {
                for (String childName : children.keySet()) {
                    if (!alreadyVisited.containsKey(childName)) {
                        stack.push(childName);
                        alreadyVisited.put(childName, children.get(childName));
                    }
                }
            }
        }
        alreadyVisited.remove(node);
        if (!alreadyVisited.isEmpty())
            return alreadyVisited;

        return null;
    }

    private static List<String> sort(List<String> permList) {

        List<String> result = new ArrayList<>();

        for (String key : permList) {
            /*
             * Ignore stupid plugins which add empty permission nodes.
             */
            if (!key.isEmpty()) {
                String a = key.charAt(0) == '-' ? key.substring(1) : key;
                Map<String, Boolean> allchildren = getAllChildren(a, new HashSet<>());
                if (allchildren != null) {

                    ListIterator<String> itr = result.listIterator();

                    while (itr.hasNext()) {
                        String node = itr.next();
                        String b = node.charAt(0) == '-' ? node.substring(1) : node;

                        // Insert the parent node before the child
                        if (allchildren.containsKey(b)) {
                            itr.set(key);
                            itr.add(node);
                            break;
                        }
                    }
                }
                if (!result.contains(key))
                    result.add(key);
            }
        }

        return result;
    }
    public static void updatePlayer(Player player, Permission permission) {
        PermissionAttachment attachment = null;
        if (loadPermissionAttachment.containsKey(player.getUniqueId())) {
            attachment = loadPermissionAttachment.get(player.getUniqueId());
        } else {
            attachment = player.addAttachment(Functions.instance);
            loadPermissionAttachment.put(player.getUniqueId(), attachment);
        }
        List<String> playerPermArray = new ArrayList<>(Functions.instance.getPlayerManager().getUser(player.getUniqueId()).getOtherPermissions());
        LinkedHashMap<String, Boolean> newPerms = new LinkedHashMap<>();

        // Sort the perm list by parent/child, so it will push to superperms
        // correctly.
        playerPermArray = sort(playerPermArray);

        boolean value;
        for (String permissio : playerPermArray) {
            value = (!permissio.startsWith("-"));
            newPerms.put((value ? permissio : permissio.substring(1)), value);
        }
        try { // Codename_B source
            synchronized (attachment.getPermissible()) {

                @SuppressWarnings("unchecked")
                Map<String, Boolean> orig = (Map<String, Boolean>) permissions.get(attachment);
                // Clear the map (faster than removing the attachment and
                // recalculating)
                orig.clear();
                // Then whack our map into there
                orig.putAll(newPerms);
                // That's all folks!
                attachment.getPermissible().recalculatePermissions();

                // Tab complete and command visibility
                // Method only available post 1.14
                updatePlayerCommand(player);

            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        attachment.setPermission(permission, hasPermissions(player, permission.getName().toLowerCase()));

    }
    public static void updatePlayer(Player player) {
        registeredPermission.forEach((name,p)->{
            updatePlayer(player,p);
        });
    }
    public static void updatePlayers() {
        Bukkit.getOnlinePlayers().forEach(PermissionsUtils::updatePlayer);
    }
    public static void collectPlayer(Player player) {
        loadPermissionAttachment.forEach((uuid, permissionAttachment) -> {
            if (uuid.equals(player.getUniqueId())) {
                collectPlayer(player);
            }
        });
    }
    public static void collectPlayer(Player player,PermissionAttachment permissionAttachment) {
        player.removeAttachment(permissionAttachment);
        loadPermissionAttachment.remove(player.getUniqueId(),permissionAttachment);
    }
    public static void collectPlayers() {
        Bukkit.getOnlinePlayers().forEach(PermissionsUtils::collectPlayer);
    }
    private static Field permissions;

    // Setup reflection (Thanks to Codename_B for the reflection source)
    static {
        try {
            permissions = PermissionAttachment.class.getDeclaredField("permissions");
            permissions.setAccessible(true);
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
