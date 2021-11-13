package org.functions.Bukkit.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Listener.Players;
import org.functions.Bukkit.Tasks.CheckAccountLogin;
import org.functions.Bukkit.Tasks.Tasks;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Functions extends JavaPlugin {
    Latest latest = null;
    public static Functions instance;
    Configuration configuration;
    DataBase database = null;
    LinkedHashMap<String,String> table = new LinkedHashMap<>();
    public AddressLocation location = null;
    public FPI getAPI() {
        return new FPI();
    }
    public void onLoad() {
        instance = this;
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        String path = getDataFolder()+"";
        path = path.replace("/","\\");
        File file = new File(path,"Logs");
        latest = new Latest(file);
        configuration = new Configuration();
        configuration.install();
        if (!(new File(getDataFolder(),"config.yml").exists())) {
            saveDefaultConfig();
            saveConfig();
            //saveDefaultConfig();
        }
        location = new AddressLocation(getConfig().getString("AddressCheck.IPImportFile", "ip.dat"),getConfig().getString("AddressCheck.Folder",getDataFolder().getAbsolutePath()));
    }
    public DataBase getDatabase() {
        return database;
    }
    public String getTable(String name) {
        return table.get(name);
    }
    public String Prefix() {
        return getConfig().getString("Prefix","§3[§bFunctions§3] §r");
    }
    public void reloadTable() {
        if (getConfiguration().getConfig().getBoolean("Debug")) {
            database.execute("drop table if exists " + getTable("Accounts"));
            database.execute("drop table if exists " + getTable("Rules"));

        }
        database.execute("create table if not exists " + getTable("Accounts") + " ( Name TEXT, LowerName TEXT, UUID TEXT, Password TEXT, IP TEXT, AutoLogin BOOLEAN DEFAULT false, RegisterTime DEFAULT CURRENT_TIMESTAMP, Mail TEXT, Position TEXT )");
        database.execute("create table if not exists " + getTable("Rules") + " ( Rules TEXT, Enable BOOLEAN DEFAULT true )");
        database.execute("create table if not exists " + getTable("Spawn") + " ( Name TEXT, Location TEXT )");
        database.execute("create table if not exists " + getTable("Economy") + " ( UUID Text, Economy DOUBLE DEFAULT 0 , Bank DOUBLE DEFAULT 0 )");
        //database.execute("create table if not exists " + getTable("Operators") + " ( UUID Text, Operator BOOLEAN DEFAULT false ) ");
    }
    public void reloadDataBase() {
        table.clear();
        table.put("Economy",getConfig().getString("DataBase.Economy","[Economy]"));
        table.put("Users", getConfig().getString("DataBase.Users", "[Users]"));
        table.put("Accounts",getConfig().getString("DataBase.Accounts", "[Accounts]"));
        table.put("Warps",getConfig().getString("DataBase.Warps","[Warps]"));
        table.put("Home",getConfig().getString("DataBase.Home","[Home]"));
        table.put("Banned",getConfig().getString("DataBase.Banned", "[Banned]"));
        table.put("Report",getConfig().getString("DataBase.Report", "[Report]"));
        table.put("Operators",getConfig().getString("DataBase.Operators","[Operators]"));
        table.put("Rules",getConfig().getString("DataBase.Rules","[Rules]"));
        table.put("Spawn",getConfig().getString("DataBase.Spawn","[Spawn]"));
        database.execute("drop table if exists [Configuration_Table]");
        database.execute("create table if not exists [Configuration_Table] ( Type TEXT, Name TEXT )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Economy', '" + getConfig().getString("DataBase.Economy","[Economy]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Users', '" + getConfig().getString("DataBase.Users", "[Users]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Accounts', '" + getConfig().getString("DataBase.Accounts", "[Accounts]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Warps', '" + getConfig().getString("DataBase.Warps","[Warps]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Home', '" + getConfig().getString("DataBase.Home","[Home]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Banned', '" + getConfig().getString("DataBase.Banned", "[Banned]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Report', '" + getConfig().getString("DataBase.Report", "[Report]") + "' )");
        database.execute("INSERT INTO [Configuration_Table] ( Type, Name ) VALUES ( 'Operators', '" + getConfig().getString("DataBase.Operators", "[Operators]") + "' )");
    }
    public void reloadCommand() {
    }
    public File getFolder() {
        return getDataFolder();
    }
    public void savefile(String name, boolean replace) {
        saveResource(name, replace);
    }
    public void onEnable() {
        new Metrics(this,11673);
        instance = this;
        print(configuration.getSettings().getString("Mail.From"));
        if (getConfig().getString("DataBase.Type").equals("MYSQL")) {
            //database = new MySql(getConfig().getString("SaveFile.MySql.database","functions"),getConfig().getString("SaveFile.MySql.User","root"),getConfig().getString("SaveFile.MySql.Password","root"));
        } else if (getConfig().getString("DataBase.Type").equals("SQLITE")) {
            database = new Sql(getDataFolder() + "/" + getConfig().getString("DataBase.File","DataBase.db"));
            database.init();
            database.connect();
            //database.connect();
        }
        reloadDataBase();
        reloadTable();
        new FPI().registerCommand();
        new FPI().registerListener();
        runScheduler();
        configuration.onQQAddress();
        // Plugin startup logic

    }
    public void runScheduler() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CheckAccountLogin(), 0, 20*5);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Tasks(), 0, 0);
    }
    public void print(Object text) {
        getServer().getConsoleSender().sendMessage(Prefix() + text);
        latest.print(text);
    }
    public void onDisable() {
        // 获取在线玩家
        for (Player p : getAPI().getOnlinePlayers()) {
            // Bukkit 事件新建一个
            PlayerQuitEvent event = new PlayerQuitEvent(p, ChatColor.YELLOW + p.getName() + " left the game");
            // 给自己开一个监听器
            new Players().leave(event);
            // code
//            @EventHandler
//            public void leave(PlayerQuitEvent event) {
//                Player p = event.getPlayer();
//                account = new Account(p.getUniqueId());
//                if (account.exists() || account.isLogin()) {
//                    Accounts.login.remove(p.getUniqueId());
//                    if (fpi.cps.get(p.getUniqueId())!=null) fpi.cps.remove(p.getUniqueId());
//                    account.setPosition();
//                    account.teleportSpawn();
//                }
//
//            }
            // 然后完成离开
            print("Successfully execute " + p.getName()  + " event.");
        }
        instance = null;
        database.disconnect();
        // Plugin shutdown logic
    }
    public void Language() {
        File file = new File(getDataFolder(),"Language-" + getConfig().getString("Language") + ".yml");

    }
    public Configuration getConfiguration() {
        return configuration;
    }
}