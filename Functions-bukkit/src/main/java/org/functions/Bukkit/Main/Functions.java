package org.functions.Bukkit.Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.API.Hook.PlaceholderAPIHook;
import org.functions.Bukkit.Listener.Players;
import org.functions.Bukkit.Main.Server.FServer;
import org.functions.Bukkit.Main.functions.ForceLoad.Force;
import org.functions.Bukkit.Main.functions.Utitils.NMSClassStorage;
import org.functions.Bukkit.Main.functions.YamlUsers;
import org.functions.Bukkit.Main.functions.AddressLocation;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCordTeleport;
import org.functions.Bukkit.Main.functions.ServerTitle;
import org.functions.Bukkit.Tasks.*;

import java.io.File;
import java.util.*;

public final class Functions extends JavaPlugin {
    NMSClassStorage clazz = new NMSClassStorage();
    YamlUsers yamlUsers;
    BungeeCordTeleport bungeeCordTeleport;
    FServer f;
    public ServerTitle title;
    //public PermissionsUtils.BukkitPermissions perms = new PermissionsUtils.BukkitPermissions();
    PlayerManager pm;
    Latest latest = null;
    public static Functions instance;
    Configuration configuration;
    public DataBase database = null;
    LinkedHashMap<String,String> table = new LinkedHashMap<>();
    public AddressLocation location = null;
    FPI fpi;
    public NMSClassStorage getClassesStorage() {
        return clazz;
    }
    public FPI getAPI() {
        return fpi;
    }
    public void onLoad() {
        instance = this;
        fpi = new FPI();
        File database = new File(getDataFolder(),"DataBase.db");
        if (database.exists()) database.deleteOnExit();
        if (!(new File(getDataFolder(),"config.yml").exists())) {
            //saveConfig();
            saveDefaultConfig();
        }
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        File file = new File(path,"Logs");
        latest = new Latest(file);
        configuration = new Configuration();
        configuration.install();
        reloadConfig();
        location = new AddressLocation(getConfig().getString("AddressCheck.IPImportFile", "ip.dat"),getConfig().getString("AddressCheck.Folder",getDataFolder().getAbsolutePath()));
        file = new File(path,"Users");
        yamlUsers = new YamlUsers(file);
    }
    public File getDirPath(String dir) {
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        return new File(path,dir);
    }
    public File getDirPath(File path,String dir) {
        String paths = getDataFolder()+"";
        paths = paths.replace("\\","/");
        return new File(paths,dir);
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
        if (getConfiguration().getConfig().getBoolean("Debug",false)) {
            database.execute("drop table if exists " + getTable("Accounts"));
            database.execute("drop table if exists " + getTable("Rules"));
            database.execute("drop table if exists " + getTable("Economy"));
            database.execute("drop table if exists " + getTable("Users"));

        }

        database.execute("create table if not exists " + getTable("Accounts") + " ( Name TEXT, LowerName TEXT, UUID TEXT, Password TEXT, IP TEXT, AutoLogin TEXT DEFAULT false, RegisterTime DEFAULT CURRENT_TIMESTAMP, Mail TEXT, Position TEXT, GameMode TEXT, AllowFight TEXT Default false)");
        database.execute("create table if not exists " + getTable("Rules") + " ( Rules TEXT, Enable BOOLEAN DEFAULT true )");
        database.execute("create table if not exists " + getTable("Spawn") + " ( Name TEXT, Location TEXT )");
        database.execute("create table if not exists " + getTable("Economy") + " ( UUID TEXT, Economy DOUBLE DEFAULT 0 , Bank DOUBLE DEFAULT 0 )");
        database.execute("create table if not exists " + getTable("Users") + " ( UUID TEXT, 'Group' TEXT DEFAULT 'Default', Prefixes TEXT, Prefix TEXT, Suffixes TEXT, Suffix TEXT, Permissions TEXT, Hide BOOLEAN DEFAULT false)");

        database.execute("create table if not exists " + getTable("Operators") + " ( UUID Text, Operator BOOLEAN DEFAULT false ) ");
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
    Force forceLoad;
    public Force getForceLoad() {
        return forceLoad;
    }
    public File getFolder() {
        return getDataFolder();
    }
    public void savefile(String name, boolean replace) {
        saveResource(name, replace);
    }
    public void onEnable() {
        getAPI().registerCommand();
        getAPI().registerListener();
        Metrics metrics = new Metrics(this, 11673);
        instance = this;
        f = new FServer(getServer());
        print(configuration.getSettings().getString("Mail.From"));
        if (getConfig().getString("DataBase.Type").equals("MYSQL")) {
            //database = new MySql(getConfig().getString("MySql.database","functions"),getConfig().getString("MySql.User","root"),getConfig().getString("MySql.Password","root"));
        } else if (getConfig().getString("DataBase.Type").equals("SQLITE")) {
            database = new Sql(getDataFolder() + "/" + getConfig().getString("DataBase.File", "DataBase.db"));
            database.init();
            database.connect();
            //database.connect();
        }
        reloadDataBase();
        reloadTable();
        pm = new PlayerManager(getServer());
        //a = new AccountMessaging();
        //a.onEnable();
        runScheduler();
        title = new ServerTitle();
        configuration.onQQAddress();
        if (getAPI().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Functions.instance.print("Now! Registering placeholder api hook");
            new PlaceholderAPIHook().register();
            Functions.instance.print("Successfully register placeholder api hook.");
        }
        forceLoad = new Force(f);
        print("Is BungeeCord: " + f.isBc());
        print("Plugin folder size: " + configuration.DirSize());
        bungeeCordTeleport = new BungeeCordTeleport();
        bungeeCordTeleport.onEnable();
        f.flushMemory();
        // recipe
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("packet_ice"),new ItemStack(Material.PACKED_ICE)).shape("xxx","xxx","xxx").setIngredient('x',Material.ICE);
        getServer().addRecipe(recipe);
        //f.clearEnderManAndEnderPearlItem();
        // Plugin startup logic
    }
    public YamlUsers yamlUsers() {
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        File file = new File(path,"Users");
        return yamlUsers != null ? yamlUsers : new YamlUsers(file);
    }
    public FServer getFServer() {
        return f;
    }
    Task task = new Task();
    public void runScheduler() {
        task.runTask(new BalanceTopRunnable(),0);
        task.runTask(pm,0);
        task.runTask(new sendPacketToClient(),0);
        task.runTask(new AnimationsTask(),0);
        task.runTask(new CheckAccountLogin(),20 * getConfig().getLong("Functions.RegisterLoginMessageInterval",5));
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, new CheckAccountLogin(), 0, 20 * getConfig().getLong("Functions.RegisterLoginMessageInterval",5));
       // getServer().getScheduler().scheduleSyncRepeatingTask(this, new Tasks(), 0, 0);
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, new BalanceTopRunnable(), 0, 0);
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, pm, 0, 0);
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, new sendPacketToClient(), 0, 0);
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, new AnimationsTask(), 0, 0);
    }
    public void print(Object text) {
        getServer().getConsoleSender().sendMessage(Prefix() + text);
        latest.print(text);
    }
    public void inLogs(Object text) {
        //getServer().getConsoleSender().sendMessage(Prefix() + text);
        latest.print(text);
    }
    public PlayerManager getPlayerManager() {
        return pm;
    }
    public void print(Object text,String type) {
        getServer().getConsoleSender().sendMessage(Prefix() + type + " " + text);
        latest.print(type.toString() + " " + text.toString());
    }
    public void print(Object text,final Class<?> type) {
        getServer().getConsoleSender().sendMessage(Prefix() + type.getName() + " " + text);
        latest.print(type.getName() + " " + text);
    }

    public BungeeCordTeleport getMessaging() {
        return bungeeCordTeleport;
    }

    public void onDisableSettings() {
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
    }
    public void onDisable() {
        onDisableSettings();
        bungeeCordTeleport.onDisable();
        bungeeCordTeleport = null;
        // 获取在线玩家
        //a.onDisable();
        title = null;
        instance = null;
        database.disconnect();
        database = null;
        // Plugin shutdown logic
    }
    public Configuration getConfiguration() {
        return configuration;
    }
}