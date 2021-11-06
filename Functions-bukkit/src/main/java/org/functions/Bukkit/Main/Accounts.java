package org.functions.Bukkit.Main;

import org.functions.Bukkit.API.FPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class Accounts {
    public static LinkedHashMap<UUID, Boolean> login = new LinkedHashMap<>();
    static String table = Functions.instance.getTable("Accounts");
    static DataBase db = Functions.instance.getDatabase();
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
        return new FPI().putLanguage("AccountNotInServer","&c服务器没有开启登陆注册账号功能！");
    }
    public static String noRegister() {
        return new FPI().putLanguage("AccountNotRegister","&c服务器没有开启注册账号功能！");
    }
    public static boolean NoLoginTeleportSpawn() {
        return Functions.instance.getConfiguration().getSettings().getBoolean("Login.NoLoginTeleportSpawn",true);
    }
    public static boolean LoginTeleportSpawn() {
        return Functions.instance.getConfiguration().getSettings().getBoolean("Login.LoginTeleportSpawn",true);
    }
}
