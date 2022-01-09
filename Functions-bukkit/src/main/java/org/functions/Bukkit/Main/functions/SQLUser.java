package org.functions.Bukkit.Main.functions;

import com.google.gson.JsonArray;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.API.SpeedPerSeconds;
import org.functions.Bukkit.Main.DataBase;
import org.functions.Bukkit.Main.Functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLUser implements IUser {
    UUID uuid;
    DataBase db = Functions.instance.database;
    String table = Functions.instance.getTable("Users");
    String select;
    Group group = null;
    List<String> permissions = new ArrayList<>();
    List<String> prefixes = new ArrayList<>();
    List<String> suffixes = new ArrayList<>();
    String prefix = "";
    String suffix = "";
    boolean hide = false;
    public String select_all = "SELECT * FROM " + Functions.instance.getTable("Users");
    Utils.sendTellRaw send;
    public SQLUser(UUID uuid) {
        this.uuid = uuid;
        if (!exists()) {
            db.execute("INSERT INTO " + table + " ( UUID ) VALUES ( '" + uuid.toString() + "' )");
        }
        select = "SELECT * FROM " + table + " WHERE UUID='" + uuid.toString() + "'";
        try {
            group = new Group(db.query(select).getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        send = new Utils.sendTellRaw(Bukkit.getPlayer(uuid));
    }
    public SQLUser(Player player) {
        this.uuid = player.getUniqueId();
        if (!exists()) {
            db.execute("INSERT INTO " + table + " ( UUID ) VALUES ( '" + uuid.toString() + "' )");
        }
        select = "SELECT * FROM " + table + " WHERE UUID='" + uuid.toString() + "'";
        try {
            group = new Group(db.query(select).getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        send = new Utils.sendTellRaw(Bukkit.getPlayer(uuid));
    }
    public SQLUser(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        if (!exists()) {
            db.execute("INSERT INTO " + table + " ( UUID ) VALUES ( '" + uuid.toString() + "' )");
        }
        select = "SELECT * FROM " + table + " WHERE UUID='" + uuid.toString() + "'";
        try {
            group = new Group(db.query(select).getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //send = new Utils.sendTellRaw(Bukkit.getPlayer(uuid));
    }
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
        db.execute("UPDATE " + table + " SET Permissions='" + ListToString(permissions) + "' where UUID='" + uuid.toString() + "'");
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
    public List<String> getPermissions() {
        if (DelayGet.users.get(uuid.toString()+"getPermissions")!=null) {
            if (DelayGet.users.get(uuid.toString()+"getPermissions") <= System.currentTimeMillis()) {
                return permissions;
            }
            DelayGet.users.remove(uuid.toString()+"getPermissions");
            DelayGet.users.put(uuid.toString()+"getPermissions",System.currentTimeMillis() + 5000);
        }
        permissions = getGroup().getAllPermissions();
        try {
            if (db.query(select).getString("Permissions")!=null) permissions = StringToList(db.query(select).getString("Permissions"));
            return permissions;
        } catch (SQLException e) {
            e.printStackTrace();
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
        db.execute("UPDATE " + table + " SET Prefixes='" + ListToString(prefixes) + "' where UUID='" + uuid.toString() + "'");
    }
    public List<String> getPrefixes() {
        if (DelayGet.users.get(uuid.toString()+"getPrefixes")!=null) {
            if (DelayGet.users.get(uuid.toString()+"getPrefixes") <= System.currentTimeMillis()) {
                return prefixes;
            }
            DelayGet.users.remove(uuid.toString()+"getPrefixes");
            DelayGet.users.put(uuid.toString()+"getPrefixes",System.currentTimeMillis() + 5000);
        }
        try {
            prefixes = StringToList(db.query(select).getString("Prefixes"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefixes;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        db.execute("UPDATE " + table + " SET Prefix='" + prefix + "' where UUID='" + uuid.toString() + "'");
    }
    public String getPrefix() {
        if (DelayGet.users.get(uuid.toString()+"getPrefix")!=null) {
            if (DelayGet.users.get(uuid.toString()+"getPrefixes") <= System.currentTimeMillis()) {
                return prefix;
            }
            DelayGet.users.remove(uuid.toString()+"getPrefix");
            DelayGet.users.put(uuid.toString()+"getPrefix",System.currentTimeMillis() + 5000);
        }
        prefix = Functions.instance.getAPI().replace(getGroup().getPrefix(),getPlayer());
        try {
            String t = db.query(select).getString("Prefix");
            if (t!=null) prefix = Functions.instance.getAPI().replace(t,getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix;
    }
    public void setSuffixes(List<String> Suffixes) {
        this.suffixes = Suffixes;
        db.execute("UPDATE " + table + " SET Suffixes='" + ListToString(Suffixes) + "' where UUID='" + uuid.toString() + "'");
    }
    public List<String> getSuffixes() {
        if (DelayGet.users.get(uuid.toString()+"getSuffixes")!=null) {
            if (DelayGet.users.get(uuid.toString()+"getSuffixes") <= System.currentTimeMillis()) {
                return suffixes;
            }
            DelayGet.users.remove(uuid.toString()+"getSuffixes");
            DelayGet.users.put(uuid.toString()+"getSuffixes",System.currentTimeMillis() + 5000);
        }
        try {
            suffixes = StringToList(db.query(select).getString("Suffixes"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suffixes;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        db.execute("UPDATE " + table + " SET Suffix='" + suffix + "' where UUID='" + uuid.toString() + "'");
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
        if (DelayGet.users.get(uuid.toString()+"getSuffix")!=null) {
            if (DelayGet.users.get(uuid.toString()+"getSuffix") <= System.currentTimeMillis()) {
                return suffix;
            }
            DelayGet.users.remove(uuid.toString()+"getSuffix");
            DelayGet.users.put(uuid.toString()+"getSuffix",System.currentTimeMillis() + 5000);
        }
        suffix = Functions.instance.getAPI().replace(getGroup().getSuffix(),getPlayer());
        try {
            String t = db.query(select).getString("Suffix");
            if (t!=null) suffix = Functions.instance.getAPI().replace(t,getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suffix;
    }
    public boolean addPrefixes(String name) {
        //boolean is = false;
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
        //boolean is = false;
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
        boolean is = true;
        String s = null;
        if (getSuffixes().size() >= number) {
            List<String> ls = getSuffixes();
            s = ls.remove(number);
            setSuffixes(ls);
        }
        return s;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(String name) {
        db.execute("UPDATE " + table + " Set 'Group'='" + name + "' where UUID='" + uuid.toString() + "'");
        group = new Group(name);
    }
    public Economy getEconomy() {
        return new Economy(uuid);
    }
    public Bank getBank() {
        return new Bank(uuid);
    }
    public Account getAccount() {
        return new Account(uuid);
    }
    public OfflinePlayer getOfflinePlayer() {
        return Functions.instance.getAPI().getServer().getOfflinePlayer(uuid);
    }
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }
    public boolean exists() {
        List<String> ls = new ArrayList<>();
        ResultSet rs = db.query(select_all);
        if (rs == null) {
            return false;
        }
        try {
            while (rs.next()) {
                ls.add(rs.getString("UUID"));
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }
        for (String s : ls) {
            if (s.equalsIgnoreCase(uuid.toString())) {
                ls.clear();
                return true;
            }
        }
        ls.clear();
        return false;
    }
    public ClickPerSeconds getCPS() {
        return Functions.instance.getAPI().cps.get(getPlayer().getUniqueId());
    }

    public SpeedPerSeconds getSPS() {
        return null;//Functions.instance.getAPI().sps.get(getPlayer().getUniqueId());
    }

    public boolean isHiding() {
        if (DelayGet.users.get(uuid.toString()+"isHiding")!=null) {
            if (DelayGet.users.get(uuid.toString()+"isHiding") <= System.currentTimeMillis()) {
                return hide;
            }
            DelayGet.users.remove(uuid.toString()+"isHiding");
            DelayGet.users.put(uuid.toString()+"isHiding",System.currentTimeMillis() + 5000);
        }
        try {
            hide = Boolean.parseBoolean(db.query(select).getString("Hide"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hide;
    }
    public void setInvisibility(boolean invisibility) {
        db.execute("UPDATE " + table + " Set 'Hide'='" + invisibility + "' where UUID='" + uuid.toString() + "'");

    }
    public String getDisplayName() {
        return getPrefix() + getPlayer().getName() + getSuffix();
    }
    public void sendTellRaw(String text) {
        if (Functions.instance.getServer().getOfflinePlayer(uuid).isOnline()) send.send(text);
    }
    public void sendParseTellRaw(String text) {
        if (Functions.instance.getServer().getOfflinePlayer(uuid).isOnline()) send.send(send.parse(text));
    }
    public String getJsonChatDisplayName() {
        return "{\"text\":\"" + getDisplayName() + "%lines%UUID: " + uuid.toString() + "%lines%金币" + getEconomy().getBalance() + "%lines%银行" + getBank().getBalance() + "\"}";
    }

    public String getName() {
        return getOfflinePlayer().getName();
    }

}
