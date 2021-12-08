package org.functions.Bukkit.Main.functions;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.DataBase;
import org.functions.Bukkit.Main.Functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public GameMode getGameMode() {
        if (exists()) {
            try {
                return GameMode.valueOf(db.query(select).getString("GameMode"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Bukkit.getDefaultGameMode();
    }
    public boolean AllowFight() {
        if (exists()) {
            try {
                return Boolean.parseBoolean(db.query(select).getString("AllowFight"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public void setAllowFight() {
        db.execute("UPDATE " + table + " set AllowFight='" + getPlayer().getAllowFlight() + "' where UUID='" + uuid.toString() + "'");
    }
    public void setGameMode() {
        db.execute("UPDATE " + table + " set GameMode='" + getPlayer().getGameMode().toString() + "' where UUID='" + uuid.toString() + "'");
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
    String matches = "([a-zA-Z0-9]*)@([a-zA-Z0-9]*)\\.([a-zA-Z0-9]*)";
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
        return Functions.instance.getAPI().getServer().getPlayer(uuid);
    }
    public String address() {
        return getPlayer().getPlayer().getAddress().getAddress().getHostAddress();
    }
    public boolean Register(String password) {
        if (!exists()) {
            password = security.security(password);
            db.execute("INSERT INTO " + this.table + " ( Name, LowerName, UUID, Password, IP, GameMode, AllowFight) VALUES ( '" + getPlayer().getName() + "', '" + getPlayer().getName().toLowerCase() + "', '" + uuid.toString() + "', '" + password + "', '" + getPlayer().getPlayer().getAddress().getAddress().getHostAddress() + "', '" + Bukkit.getDefaultGameMode() + "', 'false')");
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
            if (getPlayer()!=null) logout();
            db.execute("DELETE FROM " + table + " WHERE UUID='" + uuid.toString() + "'");
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
                setGameMode();
                getPlayer().setGameMode(GameMode.valueOf(Functions.instance.getConfiguration().getConfig().getString("Functions.NotLoginGameMode",Bukkit.getDefaultGameMode().toString())));
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
                        getPlayer().setGameMode(getGameMode());
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean setAutoLogin() {
        if (getAutoLogin()) {
            db.execute("UPDATE " + table + " set AutoLogin='" + false + "' where UUID='" + uuid.toString() + "';");
            return true;
        }
        db.execute("UPDATE " + table + " set AutoLogin='" + true + "' where UUID='" + uuid.toString() + "';");
        return true;
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
    public String getLowerName() {
        try {
            return db.query(select).getString("LowerName");
        } catch (SQLException var2) {
            var2.printStackTrace();
            return "steve";
        }
    }
    public String getName() {
        try {
            return db.query(select).getString("Name");
        } catch (SQLException var2) {
            var2.printStackTrace();
            return "Steve";
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
    public boolean WrongPassword() {
        if (Functions.instance.getAPI().getRules().isEnabled(FunctionsRules.Type.WRONGPASSWORD)) {
            getPlayer().sendMessage(Functions.instance.getAPI().putLanguage("AccountPrintWrongPassword","&c您输入的的密码错误%lines%请找管理员！",getPlayer()));
            getPlayer().kickPlayer(Functions.instance.getAPI().putLanguage("AccountPrintWrongPassword","&c您输入的的密码错误%lines%请找管理员！",getPlayer()));
        }
        return false;
    }
    public boolean Login(String password) {
        if (exists()) {
            if (!isLogin()) {
                try {
                    password = security.security(password);
                    if (db.query(select).getString("Password").equals(password)) {
                        Accounts.login.put(uuid, true);
                        setAddress();
                        teleportQuitPosition();
                        getPlayer().setGameMode(getGameMode());
                        return true;
                    } else {
                        WrongPassword();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean mailLogin() {
        if (exists()) {
            if (!isLogin()) {
                Accounts.login.put(uuid, true);
                setAddress();
                teleportQuitPosition();
                getPlayer().setGameMode(getGameMode());
                return true;
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
                    logout();
                    return true;
                }
                return false;
        }
        return false;
    }
}
