package org.functions.Bukkit.Main.functions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.Main.DataBase;
import org.functions.Bukkit.Main.Functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class User {
    UUID uuid;
    DataBase db = Functions.instance.database;
    String table = Functions.instance.getTable("Users");
    String select;
    Group group = null;
    public String select_all = "SELECT * FROM " + Functions.instance.getTable("Users");
    public User(UUID uuid) {
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

    }
    public void setPermissions(List<String> permissions) {
        db.execute("UPDATE " + table + " SET Permissions='" + permissions.toString() + "' where UUID='" + uuid.toString() + "'");
    }
    public List<String> getPermissions() {
        try {
            if (db.query(select).getString("Permissions")!=null) return Collections.singletonList(db.query(select).getString("Permissions"));
            return getGroup().getAllPermissions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getGroup().getAllPermissions();
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
        db.execute("UPDATE " + table + " SET Prefixes='" + prefixes.toString() + "' where UUID='" + uuid.toString() + "'");
    }
    public List<String> getPrefixes() {
        try {
            return Collections.singletonList(db.query(select).getString("Prefixes"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setPrefix(String prefix) {
        db.execute("UPDATE " + table + " SET Prefix='" + prefix + "' where UUID='" + uuid.toString() + "'");
    }
    public String getPrefix() {
        try {
            if (db.query(select).getString("Prefix")==null) return Functions.instance.getAPI().replace(getGroup().getPrefix(),getPlayer());
            return Functions.instance.getAPI().replace(db.query(select).getString("Prefix"),getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Functions.instance.getAPI().replace(getGroup().getPrefix(),getPlayer());
    }
    public void setSuffixes(List<String> Suffixes) {
        db.execute("UPDATE " + table + " SET Suffixes='" + Suffixes.toString() + "' where UUID='" + uuid.toString() + "'");
    }
    public List<String> getSuffixes() {
        try {
            return Collections.singletonList(db.query(select).getString("Suffixes"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setSuffix(String prefix) {
        db.execute("UPDATE " + table + " SET Suffix='" + prefix + "' where UUID='" + uuid.toString() + "'");
    }
    public String getSuffix() {
        try {
            if (db.query(select).getString("Suffix")==null) return Functions.instance.getAPI().replace(getGroup().getSuffix(),getPlayer());
            return Functions.instance.getAPI().replace(db.query(select).getString("Suffix"),getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Functions.instance.getAPI().replace(getGroup().getSuffix(),getPlayer());
    }
    public Group getGroup() {
        return group;
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
}
