package org.functions.Bukkit.Main.functions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
    public static boolean hasPermissions(Player player, String Permission) {
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
                        if (t.length == i || pm.length == i) {
                            if (t[i - 1].equalsIgnoreCase(pm[i - 1])) {
                                if (t[i].startsWith("*")) {
                                    return true;
                                }
                            }
                            return false;
                        }
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
    public static boolean hasPermissionsSendMessage(Player player, String Permission) {
        if (!hasPermissions(player,Permission)) {
            sendMessagePermissions(player, Permission);
            return false;
        }
        return true;
    }
    public static class BukkitPermissions {

        protected LinkedHashMap<String, PermissionAttachment> attachments = new LinkedHashMap<>();
        protected LinkedHashMap<String, Permission> registeredPermissions = new LinkedHashMap<>();
        protected Functions plugin;
        private boolean player_join = false;

        private boolean hasUpdateCommand;

        /**
         * @return the player_join
         */
        public boolean isPlayer_join() {

            return player_join;
        }

        /**
         * @param player_join the player_join to set
         */
        public void setPlayer_join(boolean player_join) {

            this.player_join = player_join;
        }

        /**
         * Does the server support Player.updateCommand().
         *
         * @return true/false
         */
        public boolean hasUpdateCommand() {

            return hasUpdateCommand;
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

        public BukkitPermissions() {

            this.plugin = Functions.instance;
            this.reset();

            try {
                // Method only available post 1.14
                Player.class.getMethod("updateCommands");
                hasUpdateCommand = true;
            } catch (Exception ex) {
                // Server too old to support updateCommands.
                hasUpdateCommand = false;
            }
        }

        public void reset() {

            /*
             * collect new permissions
             * and register all attachments.
             */
            this.collectPermissions();
            this.updateAllPlayers();
        }

        public void collectPermissions() {

            registeredPermissions.clear();

            for (Permission perm : Bukkit.getPluginManager().getPermissions()) {
                registeredPermissions.put(perm.getName().toLowerCase(), perm);
            }

        }

        public void updatePermissions(Player player) {

            this.updatePermissions(player,null);
        }

        /**
         * Push all permissions which are registered with GM for this player, on
         * this world to Bukkit and make it update for the child nodes.
         *
         * @param player
         */
        public void updatePermissions(Player player,Object a) {

            if (player == null ) {
                return;
            }

            UUID uuid = player.getUniqueId();

            // Reset the User objects player reference.

            PermissionAttachment attachment;

            // Find the players current attachment, or add a new one.
            if (this.attachments.containsKey(uuid.toString())) {
                attachment = this.attachments.get(uuid.toString());
            } else {
                attachment = player.addAttachment(Functions.instance);
                this.attachments.put(uuid.toString(), attachment);
            }


            // Add all permissions for this player (GM only)
            // child nodes will be calculated by Bukkit.
            List<String> playerPermArray = plugin.getPlayerManager().getUser(uuid).getOtherPermissions();
            LinkedHashMap<String, Boolean> newPerms = new LinkedHashMap<>();

            // Sort the perm list by parent/child, so it will push to superperms
            // correctly.
            playerPermArray = sort(playerPermArray);

            boolean value;
            for (String permission : playerPermArray) {
                value = (!permission.startsWith("-"));
                newPerms.put((value ? permission : permission.substring(1)), value);
            }

            /*
             * Do not push any perms to bukkit if...
             * We are in offline mode
             * and the player has the 'groupmanager.noofflineperms' permission.
             */
            if (!Bukkit.getServer().getOnlineMode()
                    && (newPerms.containsKey("groupmanager.noofflineperms") && (newPerms.get("groupmanager.noofflineperms")))) {
                removeAttachment(uuid.toString());
                return;
            }


            /**
             * This is put in place until such a time as Bukkit pull 466 is
             * implemented https://github.com/Bukkit/Bukkit/pull/466
             */
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
                    if (hasUpdateCommand())
                        updateCommands(player);

                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        @SuppressWarnings("all")
        public void updateCommands(Player player) {
            if (hasUpdateCommand()) {
                try {
                    player.getClass().getMethod("updateCommands").invoke(player,null);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Sort a permission node list by parent/child
         *
         * @param permList
         * @return List sorted for priority
         */
        private List<String> sort(List<String> permList) {

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

        /**
         * Fetch all permissions which are registered with superperms.
         * {can include child nodes)
         *
         * @param includeChildren
         * @return List of all permission nodes
         */
        public List<String> getAllRegisteredPermissions(boolean includeChildren) {

            List<String> perms = new ArrayList<>();

            for (String key : registeredPermissions.keySet()) {
                if (!perms.contains(key)) {
                    perms.add(key);

                    if (includeChildren) {
                        Map<String, Boolean> children = getAllChildren(key, new HashSet<>());
                        if (children != null) {
                            for (String node : children.keySet())
                                if (!perms.contains(node))
                                    perms.add(node);
                        }
                    }
                }

            }
            return perms;
        }

        /**
         * Returns a map of ALL child permissions registered with bukkit
         * null is empty
         *
         * @param node
         * @param playerPermArray current list of perms to check against for
         *                        negations
         * @return Map of child permissions
         */
        public Map<String, Boolean> getAllChildren(String node, Set<String> playerPermArray) {

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

        /**
         * Returns a map of the child permissions (1 node deep) as registered with
         * Bukkit.
         * null is empty
         *
         * @param node
         * @return Map of child permissions
         */
        public Map<String, Boolean> getChildren(String node) {

            Permission perm = registeredPermissions.get(node.toLowerCase());
            if (perm == null)
                return null;

            return perm.getChildren();

        }

        /**
         * List all effective permissions for this player.
         *
         * @param player
         * @return List<String> of permissions
         */
        public List<String> listPerms(Player player) {

            List<String> perms = new ArrayList<>();

            /*
             * // All permissions registered with Bukkit for this player
             * PermissionAttachment attachment = this.attachments.get(player);
             *
             * // List perms for this player perms.add("Attachment Permissions:");
             * for(Map.Entry<String, Boolean> entry :
             * attachment.getPermissions().entrySet()){ perms.add(" " +
             * entry.getKey() + " = " + entry.getValue()); }
             */

            perms.add("Effective Permissions:");
            for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                if (info.getValue())
                    perms.add(" " + info.getPermission() + " = " + info.getValue());
            }
            return perms;
        }

        /**
         * force Bukkit to update every OnlinePlayers permissions.
         */
        public void updateAllPlayers() {

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updatePermissions(player);
            }
        }

        /**
         * force Bukkit to update this Players permissions.
         */
        public void updatePlayer(Player player) {

            if (player != null)
                this.updatePermissions(player, null);
        }

        /**
         * Force remove any attachments
         *
         * @param uuid
         */
        public void removeAttachment(String uuid) {

            if (attachments.containsKey(uuid)) {
                attachments.get(uuid).remove();
                attachments.remove(uuid);
            }
        }

        /**
         * Remove all attachments in case of a restart or reload.
         */
        public void removeAllAttachments() {

            /*
             * Remove all attachments.
             */
            for (String key : attachments.keySet()) {
                attachments.get(key).remove();
            }
            attachments.clear();
        }
    }
}
