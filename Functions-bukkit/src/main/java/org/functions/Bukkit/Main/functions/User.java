package org.functions.Bukkit.Main.functions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.API.SpeedPerSeconds;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.UserAccounts.Account;
import org.functions.Bukkit.Main.functions.UserAccounts.Accounts;
import org.functions.Bukkit.Main.functions.Utitils.Tellraw;

import java.util.*;

public class User implements IUser {
    public int noClickers = 0;
    UUID uuid;
    //DataBase db = Functions.instance.database;
    //String table = Functions.instance.getTable("Users");
    String select;
    Group group = null;
    List<String> permissions = new ArrayList<>();
    List<String> prefixes = new ArrayList<>();
    List<String> suffixes = new ArrayList<>();
    String prefix = "";
    String suffix = "";
    //public String select_all = "SELECT * FROM " + Functions.instance.getTable("Users");
    public User(UUID uuid) {
        this.uuid = uuid;
        if (!exists()) {
            Functions.instance.yamlUsers().initUserFileConfiguration(uuid);
        }
        group = getGroup();
    }
    public User(Player player) {
        this.uuid = player.getUniqueId();
        if (!exists()) {
            Functions.instance.yamlUsers().initUserFileConfiguration(uuid);
        }
        group = getGroup();
    }
    public User(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        if (!exists()) {
            Functions.instance.yamlUsers().initUserFileConfiguration(uuid);
        }
        group = getGroup();
    }
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
        Functions.instance.yamlUsers().set(uuid,"Permissions",ListToString(permissions));
    }
    public boolean addPermissions(String name) {
        boolean is = true;
        for (String e : getPermissions()) {
            if (e.equalsIgnoreCase(name)) {
                is = false;
                break;
            }
        }
        if (is) {
            List<String> ls = getPermissions();
            ls.add(name);
            setPermissions(ls);
        }
        return is;
    }
    public boolean removePermissions(String name) {
        boolean is = false;
        for (String e : getPermissions()) {
            if (e.equalsIgnoreCase(name)) {
                is = true;
                break;
            }
        }
        if (is) {
            List<String> ls = getPermissions();
            ls.remove(name);
            setPermissions(ls);
        }
        return is;
    }
    public List<String> getUserPermissions() {
        return Functions.instance.yamlUsers().configurations.get(uuid).getString("Permissions")!=null ? StringToList(Functions.instance.yamlUsers().configurations.get(uuid).getString("Permissions")) : new ArrayList<>();
    }
    public List<String> getPermissions() {
        permissions.clear();
        permissions = getGroup().getAllPermissions();
        if (Functions.instance.yamlUsers().configurations.get(uuid).getString("Permissions")!=null) {
            List<String> ls = getUserPermissions();
            permissions.forEach(e-> {
                ls.forEach(f->{
                    if (f.equalsIgnoreCase(e)) {
                        ls.remove(f);
                    }
                });
            });
            permissions.addAll(ls);
        }
        return permissions;
    }
    public List<String> getOtherPermissions() {
        List<String> temp = new ArrayList<>();
        for (String s : getPermissions()) {
            if (s.startsWith("functions")) {
                continue;
            }
            temp.add(s);
        }
        return temp;
    }
    public List<String> getFunctionsPermissions() {
        List<String> temp = new ArrayList<>();
        for (String s : getPermissions()) {
            if (!s.startsWith("functions")) {
                continue;
            }
            temp.add(s);
        }
        return temp;
    }
    public void setPrefixes(List<String> prefixes) {
        this.prefixes = prefixes;
        Functions.instance.yamlUsers().set(uuid,"Prefixes",ListToString(prefixes));
    }
    public List<String> getPrefixes() {
        prefixes = StringToList(Functions.instance.yamlUsers().configurations.get(uuid).getString("Prefixes"));
        return prefixes;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        Functions.instance.yamlUsers().set(uuid,"Prefix",prefix);
    }
    public String getPrefix() {
        String t = Functions.instance.yamlUsers().configurations.get(uuid).getString("Prefix");
        if (t==null || t.equals("")) {
            suffix = group.getPrefix();
        } else {
            suffix = Functions.instance.getAPI().replace(t, getPlayer());
        }
        return suffix;
    }
    public void setSuffixes(List<String> Suffixes) {
        this.suffixes = Suffixes;
        Functions.instance.yamlUsers().set(uuid,"Suffixes",ListToString(Suffixes));
    }
    public List<String> getSuffixes() {
        return StringToList(Functions.instance.yamlUsers().configurations.get(uuid).getString("Suffixes"));
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        Functions.instance.yamlUsers().set(uuid,"Suffix",suffix);
    }

    public String ListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append("\"").append(s).append("\"%lines%");
        }
        return sb.toString();
    }

    public List<String> StringToList(String list) {
        if (list == null || list.equalsIgnoreCase("null")) {
            return new ArrayList<>();
        }
        List<String> ls = new ArrayList<>();
        String[] t = list.split("%lines%");
        for (String s : t) {
            ls.add(s.substring(1,s.length()-1));
        }
        return ls;
    }

    public String getSuffix() {
        String t = Functions.instance.yamlUsers().configurations.get(uuid).getString("Suffix");
        if (t==null || t.equals("")) {
            suffix = group.getSuffix();
        } else {
            suffix = Functions.instance.getAPI().replace(t, getPlayer());
        }
        return suffix;
    }
    public boolean addPrefixes(String name) {
        List<String> ls = getPrefixes();
        for (String s : ls) {
            if (s.equalsIgnoreCase(name)) {
                return false;
            }
        }
        ls.add(name);
        setPrefixes(ls);
        return true;
    }
    public boolean addSuffixes(String name) {
        List<String> ls = getSuffixes();
        for (String s : ls) {
            if (s.equalsIgnoreCase(name)) {
                return false;
            }
        }
        ls.add(name);
        setSuffixes(ls);
        return true;
    }
    @Deprecated
    public boolean removePrefixes(String name) {
        boolean is = true;
        for (String s : getPrefixes()) {
            if (!s.equalsIgnoreCase(name)) {
                is = false;
                break;
            }
        }
        if (is) {
            List<String> ls = getPrefixes();
            ls.add(name);
            setPrefixes(ls);
        }
        return is;
    }
    public String removePrefixes(int number) {
        boolean is = true;
        String s = null;
        if (getPrefixes().size() >= number) {
            List<String> ls = getPrefixes();
            s = ls.remove(number);
            setPrefixes(ls);
        }
        return s;
    }
    @Deprecated
    public boolean removeSuffixes(String name) {
        boolean is = true;
        for (String s : getSuffixes()) {
            if (!s.equalsIgnoreCase(name)) {
                is = false;
                break;
            }
        }
        if (is) {
            List<String> ls = getSuffixes();
            ls.add(name);
            setPrefixes(ls);
        }
        return is;
    }
    public String removeSuffixes(int number) {
        String s = null;
        if (getSuffixes().size() >= number) {
            List<String> ls = getSuffixes();
            s = ls.remove(number);
            setSuffixes(ls);
        }
        return s;
    }
    public Group getGroup() {
        permissions.clear();
        if (group==null) return new Group(Functions.instance.yamlUsers().configurations.get(uuid).getString("Group",Functions.instance.getConfiguration().getSettings().getString("DefaultGroup","Default")));
        return group;
    }
    public void setGroup(String name) {
        Functions.instance.yamlUsers().set(uuid,"Group",name);
        group = new Group(name);
        permissions.clear();
    }
    public Economy getEconomy() {
        return new Economy(uuid);
    }
    public Bank getBank() {
        return new Bank(uuid);
    }
    public Account getAccount() {
        return Accounts.getAccount(uuid);
    }
    public OfflinePlayer getOfflinePlayer() {
        return Functions.instance.getAPI().getServer().getOfflinePlayer(uuid);
    }
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }
    public boolean exists() {
        return Functions.instance.yamlUsers().exists(uuid);
    }
    public ClickPerSeconds getCPS() {
        return Functions.instance.getAPI().cps.get(getPlayer().getUniqueId());
    }

    public SpeedPerSeconds getSPS() {
        return null;//Functions.instance.getAPI().sps.get(getPlayer().getUniqueId());
    }

    public boolean isHiding() {
        return Functions.instance.yamlUsers().configurations.get(uuid).getBoolean("Invisibility");
    }
    public void setInvisibility(boolean invisibility) {
        Functions.instance.yamlUsers().set(uuid,"Invisibility",invisibility);
    }
    public boolean isAFK() {
        return Functions.instance.yamlUsers().configurations.get(uuid).getBoolean("AwayFromBoard",false);
    }
    public void setAFK(boolean AwayFromBoard) {
        Functions.instance.yamlUsers().set(uuid,"AwayFromBoard",AwayFromBoard);
    }
    public String getDisplayName() {
        return getPrefix() + getPlayer().getName() + getSuffix();
    }
    public void sendTellRaw(String text) {
        if (Functions.instance.getServer().getOfflinePlayer(uuid).isOnline()) {
            Tellraw.send(getPlayer(),text);
        }
    }
    public String getJsonChatDisplayName() {
        return "{\"text\":\"" + getDisplayName() + "%lines%UUID: " + uuid.toString() + "%lines%金币" + getEconomy().getBalance() + "%lines%银行" + getBank().getBalance() + "\"}";
    }

    public String getName() {
        return getOfflinePlayer().getName();
    }

}
