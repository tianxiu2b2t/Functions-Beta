package org.functions.Bukkit.API;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.functions.Bukkit.API.serverPing.PingResponse;
import org.functions.Bukkit.API.serverPing.ServerAddress;
import org.functions.Bukkit.API.serverPing.ServerPinger;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.FunctionsRules;

import java.text.SimpleDateFormat;
import java.util.*;

public class FPI {
    public static LinkedHashMap<UUID, String> code = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Long> code_timeout = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Boolean> code_verify = new LinkedHashMap<>();
    public static LinkedHashMap<UUID, Boolean> fall = new LinkedHashMap<>();
    public LinkedHashMap<UUID, ClickPerSeconds> cps = new LinkedHashMap<>();
    public static LinkedHashMap<String,PluginCommand> commands = new LinkedHashMap<>();
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
    public String replace(Object msg) {
        String m = msg.toString();
        return m.replace("&","§").replace("%lines%","\n");
    }
    public PingResponse getServerList(ServerAddress address, int timeout) {
        return ServerPinger.fetchData(address, timeout);
    }
    public String putLanguage(String path, Object Default) {
        if (Functions.instance.getConfiguration().getLanguage().getString(path)==null) {
            Functions.instance.getConfiguration().getLanguage().addDefault(path, Default);
            Functions.instance.getConfiguration().getLanguage().options().copyDefaults(true);
            Functions.instance.getConfiguration().getLanguage().options().copyHeader();
            //Functions.instance.getConfiguration().getLanguage().set(path, Default);
            Functions.instance.getConfiguration().saveLanguage();
            //return Functions.instance.Prefix() + replace(Default);
        }
        return Functions.instance.Prefix() + replace(Functions.instance.getConfiguration().getLanguage().getString(path, Default.toString()));
    }
    public String NoPrefixPutLanguage(String path, Object Default) {
        putLanguage(path,Default);
        return replace(Functions.instance.getConfiguration().getLanguage().getString(path, Default.toString()));
    }
    public String noPermission(String permission) {
        return putLanguage("NotPermission","&c你没有该 %permission% 权限！").replace("%permission%",permission);
    }
    public String changeBooleanToText(boolean Boolean) {
        return Boolean ? NoPrefixPutLanguage("TextTrue","&a是") : NoPrefixPutLanguage("TextFalse","&c否");
    }
    public String onDisallowCommand(String cmd) {
         return putLanguage("DisallowCommand","&c%command% 这条指令已被管理员禁止！").replace("%command%",cmd);
    }
    public String noPlayer() {
        return putLanguage("ConsoleUses","&c你不能使用属于玩家的指令！");
    }
    public String noOperator() {
        return putLanguage("NotPermission","&c你不是管理员！");
    }
    public String noServer() {
        return putLanguage("NotPermission","&c你不能使用属于控制台的指令！");
    }
    public String subcmd() {
        return putLanguage("CommandLengthSmall","&c你的指令长度不能少于该指令需要的长度！");
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
    public void sendMessage(String path,Object Default,Player p) {
        p.sendMessage(putLanguage(path,Default));
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
}
