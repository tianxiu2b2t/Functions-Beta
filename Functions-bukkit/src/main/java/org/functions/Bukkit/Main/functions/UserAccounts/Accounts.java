package org.functions.Bukkit.Main.functions.UserAccounts;

import org.functions.Bukkit.API.FunctionsSQL.SQLMain;
import org.functions.Bukkit.API.FunctionsSQL.SQLRead;
import org.functions.Bukkit.Main.Functions;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class Accounts {
    public static File dir = Functions.instance.getDirPath("Accounts");
    public static List<Account> accounts = new ArrayList<>();
    public static LinkedHashMap<UUID, Boolean> login = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Long> bclogin = new LinkedHashMap<>();
    public static void reloadAccounts() {
        accounts.clear();
        SQLMain.getFilesNotFormat(dir).forEach(Accounts::getAccount);
    }
    public static int canLogin() {
        int i = 0;
        for (File file : SQLMain.getFiles(dir)) {
            if (file.length() != 0) {
                i++;
            }
        }
        return i;
    }
    public static List<Account> getAccounts() {
        if (accounts.size() != canLogin()) {
            reloadAccounts();
        }
        return accounts;
    }
    public static Account getAccount(UUID uuid) {
        for (Account a : accounts) {
            if (a.getUUID().equals(uuid)) {
                return a;
            }
        }
        accounts.add(new Account(uuid));
        return accounts.get(accounts.size() - 1);
    }
    public static Account getAccount(String name) {
        name = name.toLowerCase();
        for (Account account : accounts) {
            if (account.getName().toLowerCase().equalsIgnoreCase(name)) {
                return account;
            }
        }
        return null;
    }
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
