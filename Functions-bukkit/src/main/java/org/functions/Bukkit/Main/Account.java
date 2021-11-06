package org.functions.Bukkit.Main;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {
    String table = Functions.instance.getTable("Accounts");
    DataBase db = Functions.instance.getDatabase();
    UUID uuid = null;
    public String select_all = "SELECT * FROM " + table;
    public String select;
    public Account(UUID uuid) {
        this.uuid = uuid;
        select = "SELECT * FROM " + table + " WHERE UUID='" + uuid.toString() + "'";
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
    public void setPosition() {
        db.execute("UPDATE " + table + " set Position='" + Functions.instance.getAPI().changeLocationToString(getPlayer().getLocation()) + "' where UUID='" + uuid.toString() + "';");
    }
    public Location getPosition() {
        if (exists()) {
            try {
                return Functions.instance.getAPI().formatLocation(db.query(select).getString("Position"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return getSpawnPosition();
    }
    public Location getSpawnPosition() {
        return Functions.instance.getAPI().formatLocation(Functions.instance.getConfiguration().getSettings().getString("Login.Spawn",Functions.instance.getAPI().changeLocationToString(getPlayer().getWorld().getSpawnLocation())));

    }
    public boolean teleportSpawn() {
        if (Accounts.NoLoginTeleportSpawn()) {
            getPlayer().teleport(getSpawnPosition());
            return true;
        }
        return false;
    }
    public boolean teleportQuitPosition() {
        if (Accounts.LoginTeleportSpawn()) {
            getPlayer().teleport(getPosition());
            return true;
        }
        return false;
    }
    public boolean isLogin() {
        if (!Accounts.enable()) {
            return true;
        }
        if (Accounts.login.get(uuid)==null) {
            return false;
        }
        return Accounts.login.get(uuid);
    }
    String matches = "[a-zA-Z0-9]*@[a-zA-Z0-9]*.[a-zA-Z0-9]*";
    public boolean existsMail() {
        LinkedHashMap<UUID, String> mails = new LinkedHashMap<>();
        ResultSet rs = db.query(select_all);
        try {
            while (rs.next()) {
                if (rs.getString("Mail") != null) {
                    mails.put(UUID.fromString(rs.getString("UUID")), rs.getString("Mail"));
                }
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }
        return mails.get(uuid) !=null;
    }
    public String getMail() {
        if (existsMail()) {
            try {
                return db.query(select).getString("Mail");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public String getPassword() {
        if (exists()) {
            try {
                return db.query(select).getString("Password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean setMail(String mail) {
        if (!mail.matches(matches)) {
            return false;
        }
        db.execute("UPDATE " + table + " set Mail='" + mail + "' where UUID='" + uuid.toString() + "'");
        return true;
    }
    public Player getPlayer() {
        return new FPI().getServer().getPlayer(uuid);
    }
    public String address() {
        return getPlayer().getPlayer().getAddress().getAddress().getHostAddress();
    }
    public boolean Register(String password) {
        if (!exists()) {
            password = security.security(password);
            db.execute("INSERT INTO " + this.table + " ( Name, LowerName, UUID, Password, IP ) VALUES ( '" + getPlayer().getName() + "', '" + getPlayer().getName().toLowerCase() + "', '" + uuid.toString() + "', '" + password + "', '" + getPlayer().getPlayer().getAddress().getAddress().getHostAddress() + "' )");
            Accounts.login.put(uuid, true);
            return true;
        }
        return false;
    }
    public UUID getUniqueID() {
        return uuid;
    }
    public boolean delete() {
        if (exists()) {
            db.execute("DELETE * FROM " + table + " WHERE UUID='" + uuid.toString() + "'");
            return true;
        }
        return false;
    }
    public boolean logout() {
        if (exists()) {
            if (isLogin()) {
                Accounts.login.put(uuid,false);
                setPosition();
                teleportSpawn();
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean autoLogin() {
        if (exists()) {
            if (!isLogin()) {
                if (getAutoLogin()) {
                    if (address().equals(getAddress())) {
                        Accounts.login.put(uuid, true);
                        teleportQuitPosition();
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
        return false;
    }
    public boolean getAutoLogin() {
        try {
            return Boolean.parseBoolean(db.query(select).getString("AutoLogin"));
        } catch (SQLException var2) {
            var2.printStackTrace();
            return false;
        }
    }
    public String getRegisterTime() {
        try {
            return db.query(select).getString("RegisterTime");
        } catch (SQLException var2) {
            var2.printStackTrace();
            return (new SimpleDateFormat()).format(new Date(0L));
        }
    }
    public String getAddress() {
        try {
            return db.query(select).getString("IP");
        } catch (SQLException var2) {
            var2.printStackTrace();
            return "localhost";
        }
    }
    public String getAddressArea() {
        return Functions.instance.location.getCountry(getAddress());
    }
    public void setAddress() {
        db.execute("UPDATE " + table + " set IP='" + address() + "' where UUID='" + uuid.toString() + "';");
    }
    public boolean Login(String password) {
        if (exists()) {
            try {
                password = security.security(password);
                if (db.query(select).getString("Password").equals(password)) {
                    Accounts.login.put(uuid, true);
                    setAddress();
                    teleportQuitPosition();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }
    public boolean changePassword(String password) {
        if (exists()) {
                password = security.security(password);
                if (!getPassword().equals(password)) {
                    db.execute("UPDATE " + this.table + " SET Password='" + password + "' WHERE UUID='" + uuid + "'");
                    Accounts.login.remove(uuid);
                    return true;
                }
                return false;
        }
        return false;
    }
}
