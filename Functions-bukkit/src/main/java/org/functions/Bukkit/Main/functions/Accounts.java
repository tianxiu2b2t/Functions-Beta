package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.DataBase;
import org.functions.Bukkit.Main.Functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class Accounts {
    public static LinkedHashMap<UUID, Boolean> login = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Long> bclogin = new LinkedHashMap<>();
    static String table = Functions.instance.getTable("Accounts");
    static DataBase db = Functions.instance.getDatabase();
    public static boolean isBcLoginTimeOut(UUID uuid) {
        if (Functions.instance.getFServer().isBc()) {
            if (bclogin.get(uuid)!=null) {
                if (bclogin.get(uuid) < System.currentTimeMillis()) {
                    return true;
                }
            }
        }
        return true;
    }
    public static void reCountIsBcLoginTimeOut(UUID uuid) {
        if (Functions.instance.getFServer().isBc()) {
            if (bclogin.get(uuid) == null) {
                bclogin.put(uuid, System.currentTimeMillis() + 10000);
            } else if (isBcLoginTimeOut(uuid)) {
                bclogin.remove(uuid);
            }
        }
    }
    public static List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        ResultSet rs = db.query("SELECT * FROM " + table);
        try {
            while (rs.next()) {
                accounts.add(new Account(UUID.fromString(rs.getString("UUID"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    public static Account getAccount(UUID uuid) {
        return new Account(uuid);
    }
    public static int size() {
        return getAccounts().size();
    }
    public static boolean register() {
        return Functions.instance.getConfig().getBoolean("Functions.RegisterAccounts",true);
    }
    public static boolean enable() {
        return Functions.instance.getConfig().getBoolean("Functions.Login",true);
    }
    public static String noEnable() {
        return Functions.instance.getAPI().putLanguage("AccountNotInServer","&c服务器没有开启登陆注册账号功能！",null);
    }
    public static String noRegister() {
        return Functions.instance.getAPI().putLanguage("AccountNotRegister","&c服务器没有开启注册账号功能！",null);
    }
    public static boolean NoLoginTeleportSpawn() {
        return Functions.instance.getConfiguration().getSettings().getBoolean("Login.NoLoginTeleportSpawn",true);
    }
    public static boolean LoginTeleportSpawn() {
        return Functions.instance.getConfiguration().getSettings().getBoolean("Login.LoginTeleportSpawn",true);
    }
}
