package org.functions.Bukkit.API;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.functions.Bukkit.API.Hook.PlaceholderAPIHook;
import org.functions.Bukkit.API.serverPing.PingResponse;
import org.functions.Bukkit.API.serverPing.ServerAddress;
import org.functions.Bukkit.API.serverPing.ServerPinger;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.PlayerManager;
import org.functions.Bukkit.Main.Server.FWorld;
import org.functions.Bukkit.Main.functions.AnimationManager;
import org.functions.Bukkit.Main.functions.FunctionsRules;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class FPI {
    public LinkedHashMap<String, Long> send_packet = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, String> code = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Long> code_timeout = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Boolean> code_verify = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Boolean> fall = new LinkedHashMap<>();
    public LinkedHashMap<UUID, ClickPerSeconds> cps = new LinkedHashMap<>();
    public static LinkedHashMap<String,PluginCommand> commands = new LinkedHashMap<>();
    public Functions getInstance() {
        return Functions.instance;
    }
    public Server getServer() {
        return Functions.instance.getServer();
    }
    public PluginCommand command(String cmd) {
        return getServer().getPluginCommand(cmd);
    }
    public void getCommand(String s, TabExecutor te) {
        Functions.instance.getCommand(s).setExecutor(te);
        Functions.instance.getCommand(s).setTabCompleter(te);
        commands.put(s.toLowerCase(),Functions.instance.getCommand(s));
    }
    public boolean hasAliases(String cmd,String aliases) {
        cmd = cmd.toLowerCase();
        for (String s : commands.get(cmd).getAliases()) {
            if (s.equalsIgnoreCase(aliases)) {
                return true;
            }
        }
        if (commands.get(cmd).getName().equalsIgnoreCase(aliases)) {
            return true;
        }
        return false;
    }
    public FunctionsRules getRules() {
        return new FunctionsRules();
    }
    public List<Player> getOperators() {
        List<Player> players = new ArrayList<>();
        for (Player p : getOnlinePlayers()) {
            if (!p.isOp()) {
                continue;
            }
            players.add(p);
        }
        return players;
    }
    public void sendOperators(String text) {
        for (Player p : getOperators()) {
            p.sendMessage(Functions.instance.Prefix() + text);
        }
        Functions.instance.print(text);
    }
    public Holder getHolders() {
        return new Holder();
    }
    public String LocationToString(Location loc) {
        String x = getHolders().getHolder("Location").toString();
        x = x.replace("%world%", loc.getWorld().getName());
        x = x.replace("%x%", ((int)(loc.getX() / 1000.0D)) * 1000.0D + "");
        x = x.replace("%y%", ((int)(loc.getY() / 1000.0D)) * 1000.0D + "");
        x = x.replace("%z%", ((int)(loc.getZ() / 1000.0D)) * 1000.0D + "");
        x = x.replace("%yaw%", ((int)(loc.getYaw() / 1000.0F)) * 1000.0F + "");
        x = x.replace("%pitch%", ((int)(loc.getPitch() / 1000.0F)) * 1000.0F + "");
        return x;
    }
    public String BlockLocationToString(Location loc) {
        String x = getHolders().getHolder("Location").toString();
        x = x.replace("%world%", loc.getWorld().getName());
        x = x.replace("%x%", (int)loc.getX() + "");
        x = x.replace("%y%", (int)loc.getY() + "");
        x = x.replace("%z%", (int)loc.getZ()+ "");
        x = x.replace("%yaw%", (int)loc.getYaw() + "");
        x = x.replace("%pitch%", (int)loc.getPitch() + "");
        return x;
    }
    public FunctionsRules.Type getRulesType(String name) {
        return FunctionsRules.Type.valueOf(name);
    }
    public String replace(Object msg,Player player,String[] option,String[] value) {
        String m = msg.toString();
        if (option.length == value.length) {
            for (int i = 0; i < option.length; i++) {
                m = m.replace(option[i],value[i]);
            }
        }
        return replace(m,player);
    }
    public String replace(Object msg,Player player) {
        String m = msg.toString();
        if (player!=null) {
            String[] s = m.split("%");
            String temp = "";
            for (int i = 1; i < s.length; i = i+2) {
                temp = s[i].replace("functions_","");
                m = m.replace("%"+s[i]+"%",onRequest(player,temp));
            }
            if (getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                m = PlaceholderAPI.setPlaceholders(player, m);
            }
        }
        return m.replace("&","§").replace("%lines%","\n").replace("none","");
    }
    public PingResponse getServerList(ServerAddress address, int timeout) {
        return ServerPinger.fetchData(address, timeout);
    }
    public String putLanguage(String path, Object Default, Player player) {
        if (Functions.instance.getConfiguration().getLanguage().getString(path)==null) {
            Functions.instance.getConfiguration().getLanguage().addDefault(path, Default);
            Functions.instance.getConfiguration().getLanguage().options().copyDefaults(true);
            Functions.instance.getConfiguration().getLanguage().options().copyHeader();
            //Functions.instance.getConfiguration().getLanguage().set(path, Default);
            Functions.instance.getConfiguration().saveLanguage();
            //return Functions.instance.Prefix() + replace(Default);
        }
        String text = Functions.instance.Prefix() + replace(Functions.instance.getConfiguration().getLanguage().getString(path, Default.toString()),player);
        if (player != null) {
            if (getPluginManager().getPlugin("PlaceholderAPI")!=null) {
                return PlaceholderAPI.setPlaceholders(player, text);
            }
        }
        return text;
    }
    public String NoPrefixPutLanguage(String path, Object Default) {
        putLanguage(path,Default,null);
        return replace(Functions.instance.getConfiguration().getLanguage().getString(path, Default.toString()),null);
    }
    public String noPermission(String permission) {
        return putLanguage("NotPermission","&c你没有该 %permission% 权限！",null).replace("%permission%",permission);
    }
    public String changeBooleanToText(boolean Boolean) {
        return Boolean ? NoPrefixPutLanguage("TextTrue","&a是") : NoPrefixPutLanguage("TextFalse","&c否");
    }
    public String onDisallowCommand(String cmd) {
         return putLanguage("DisallowCommand","&c%command% 这条指令已被管理员禁止！",null).replace("%command%",cmd);
    }
    public String noPlayer() {
        return putLanguage("ConsoleUses","&c你不能使用属于玩家的指令！",null);
    }
    public String noOperator() {
        return putLanguage("NotPermission","&c你不是管理员！",null);
    }
    public String noServer() {
        return putLanguage("NotPermission","&c你不能使用属于控制台的指令！",null);
    }
    public String subcmd() {
        return putLanguage("CommandLengthSmall","&c你的指令长度不能少于该指令需要的长度！",null);
    }
    public PluginManager getPluginManager() {
        return getServer().getPluginManager();
    }
    public void registerEvents(Listener listener) {
        getPluginManager().registerEvents(listener,Functions.instance);
    }
    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }
    public void registerListener() {
        new Listeners(Functions.instance,"org.functions.Bukkit.Listener").register();
        //Functions.instance.print("Successfully.");
    }
    public void putsendMessage(String path,Object Default,Player p) {
        p.sendMessage(putLanguage(path,Default,p));
    }
    public void registerCommand() {
        new Commands(Functions.instance,"org.functions.Bukkit.Commands.Defaults").register();
        new Commands(Functions.instance,"org.functions.Bukkit.Commands.Permissions").register();
    }
    public Player getPlayer(UUID uuid) {
        return getServer().getPlayer(uuid);
    }
    public String getDate() {
        Date date = new Date();
        SimpleDateFormat Date = new SimpleDateFormat(Functions.instance.getConfiguration().getSettings().getString("Date-Time.Date","yyyy-MM-dd"));
        return Date.format(date);
    }
    public String getDateTime() {
        return getDate() + " " + getTime();
    }
    public String getTime(long Time) {
        Date date = new Date(Time);
        SimpleDateFormat Date = new SimpleDateFormat(Functions.instance.getConfiguration().getSettings().getString("Date-Time.DayTime","mm:ss"));
        return Date.format(date);
    }
    public String getTime() {
        Date date = new Date();
        SimpleDateFormat Date = new SimpleDateFormat(Functions.instance.getConfiguration().getSettings().getString("Date-Time.Time","HH:mm:ss"));
        return Date.format(date);
    }
    public Location formatLocation(String path) {
        String[] l = path.split(",");
        return new Location(Bukkit.getWorld(l[0]),Double.parseDouble(l[1]),Double.parseDouble(l[2]),Double.parseDouble(l[3]),Float.parseFloat(l[4]),Float.parseFloat(l[5]));
    }
    public Location changeStringToLocation(String position) {
        String[] location = position.split(",");
        return new Location(Bukkit.getWorld(location[0]),Double.parseDouble(location[1]),Double.parseDouble(location[2]),Double.parseDouble(location[3]),Float.parseFloat(location[4]),Float.parseFloat(location[5]));
    }
    public String[] changeLocationToListString(Location loc) {
        return (loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch()).split(",");
    }
    public String changeLocationToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }
    public String getPlayerAddress(Player p) {
        return Functions.instance.location.getCountry(p.getAddress().getAddress().getHostAddress());
    }
    public String getPlayerAddress(UUID uuid) {
        return getPlayerAddress(getPlayer(uuid));
    }
    public Group getGroup(String Name) {
        return new Group(Functions.instance.getConfiguration().groups.get(Name));
    }

    /************************* PlaceholderAPIHook **********************************/
    public String onRequest(OfflinePlayer player, String params,String[] option,String[] value) {
        if (option.length != value.length) {
            return onRequest(player,params);
        }
        String s = params;
        for (int i = 0; i < option.length;i++) {
            s = s.replace(option[i],value[i]);
        }
        return onRequest(player,params);
    }
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("%lines%")) {
            return "\n";
        }
        PlayerManager pm = Functions.instance.getPlayerManager();
        if (params.equalsIgnoreCase("server_day")) {
            return getInstance().getFServer().getServerStringForDays() + "";
        }
        if (params.equalsIgnoreCase("server_hour")) {
            return getInstance().getFServer().getServerStringForHours() + "";
        }
        if (params.equalsIgnoreCase("server_minute")) {
            return getInstance().getFServer().getServerStringForMinutes() + "";
        }
        if (params.equalsIgnoreCase("server_second")) {
            return getInstance().getFServer().getServerStringForSeconds() + "";
        }
        if (params.equalsIgnoreCase("server_nanasecound")) {
            return getInstance().getFServer().getServerStringForNanaSeconds() + "";
        }
        if (params.equalsIgnoreCase("server_start")) {
            return replace(getInstance().getConfiguration().getSettings().getString("ServerStart", "%server_day% %server_hour%:%server_minute%:%server_second%"), null);
        }
        for (FWorld e : getInstance().getFServer().getWorlds()) {
            if (params.equalsIgnoreCase("world_" + e.getWorld().getName().toLowerCase() + "_day")) {
                return e.getWorldStringForDayTime();
            } else if (params.equalsIgnoreCase("world_" + e.getWorld().getName().toLowerCase() + "_time")) {
                return e.getWorldStringForDayTime();
            }
        }
        if (params.equalsIgnoreCase("economy")) {
            return pm.getUser(player.getUniqueId()).getEconomy().display();
        }
        if (params.equalsIgnoreCase("bank")) {
            return pm.getUser(player.getUniqueId()).getBank().display();
        }
        if (params.equalsIgnoreCase("prefix")) {
            return pm.getUser(player.getUniqueId()).getPrefix();
        }
        if (params.equalsIgnoreCase("player_display")) {
            return pm.getUser(player.getUniqueId()).getPrefix() + player.getName() + pm.getUser(player.getUniqueId()).getSuffix() + "&r";
        }
        if (params.equalsIgnoreCase("suffix")) {
            return pm.getUser(player.getUniqueId()).getSuffix();
        }
        if (params.equalsIgnoreCase("tps")) {
            return Utils.TPS.getTPS();
        }
        if (params.equalsIgnoreCase("detail_tps")) {
            return Utils.TPS.details_tps();
        }
        if (params.equalsIgnoreCase("servername")) {
            return Functions.instance.getConfig().getString("ServerName","Unknown Server");
        }
        if (params.equalsIgnoreCase("recoverypasswordservername")) {
            return Functions.instance.getConfig().getString("RecoveryPasswordServerName", "Recovery Password Unknow Server");
        }
        if (params.equalsIgnoreCase("ping")) {
            return new Utils.Ping(player.getPlayer()).toString();
        }
        if (params.equalsIgnoreCase("cps")) {
            return pm.getUser(player.getUniqueId()).getCPS().getCountCPS()+"";
        }
        if (params.equalsIgnoreCase("max_cps")) {
            return pm.getUser(player.getUniqueId()).getCPS().getMaxCPS()+"";
        }
        if (params.startsWith("animation:")) {
            return replace(AnimationManager.getAnimation(params.replace("animation:","")).getAnimation(),player.getPlayer());
        }
        return "";//"This is params is unknown(I author is unhappy.)";
    }
    public String replaceJson(String text) {
        return text.replace("\\","\\\\").replace("\"","\\\"");
    }
    public String round(double min, double max) {
        return Double.toString(round(min / max * 100.0D, 1));
    }
    public double round(double value, int places) {
        if (places < 0) {
            return 0.0D;
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
