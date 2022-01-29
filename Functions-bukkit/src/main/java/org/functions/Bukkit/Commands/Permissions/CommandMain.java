package org.functions.Bukkit.Commands.Permissions;

import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.functions.Bank;
import org.functions.Bukkit.Main.functions.Economy;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.PermissionsUtils;
import org.functions.Bukkit.Main.functions.Utils;

import java.util.*;

public class CommandMain implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("functions", new CommandMain());
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        long start = 0;
        if (args.length == 0) {
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.main.reload")) {
                return true;
            }
            start = System.currentTimeMillis();
            Functions.instance.getConfiguration().reload();
            sender.sendMessage(Functions.instance.getAPI().putLanguage("ReloadConfiguration","&a成功重载配置！ (%time-ms% ms)",null).replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
            return true;
        }
        if (args[0].equalsIgnoreCase("reloadServer")) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reloadserver")) {
                return true;
            }
            Functions.instance.getServer().reload();
            start = System.currentTimeMillis();
            sender.sendMessage("§3[§bFunctions-ReloadServerAPI§3] §a成功重载服务器！ (%time-ms% ms)".replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
            return true;
        }
        if (args[0].equalsIgnoreCase("reloadData")) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reloaddata")) {
                return true;
            }
            start = System.currentTimeMillis();
            Functions.instance.getServer().reloadData();
            sender.sendMessage(Functions.instance.getAPI().putLanguage("ReloadMinecraftData","&a成功重载原版数据！ (%time-ms% ms)",null).replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
            return true;
        }
        if (args[0].equalsIgnoreCase("destroy_quiet")) {
            List<Material> a = new ArrayList<>();
            a.add(Material.BEDROCK);
            List<Material> s = new ArrayList<>();
            for (int i = 8; i < args.length; i++) {
                s.add(Material.valueOf(args[i]));
            }
            if (s.size() == 0) {
                sender.sendMessage(Utils.Fill.destroy(Bukkit.getWorld(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), a, true));
                return true;
            }
            sender.sendMessage(Utils.Fill.destroy(Bukkit.getWorld(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), a, s, true));
            return true;
        } else if (args[0].equalsIgnoreCase("destroy")) {
            List<Material> a = new ArrayList<>();
            a.add(Material.BEDROCK);
            List<Material> s = new ArrayList<>();
            for (int i = 8; i < args.length; i++) {
                s.add(Material.valueOf(args[i]));
            }
            if (s.size() == 0) {
                sender.sendMessage(Utils.Fill.destroy(Bukkit.getWorld(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), a, false));
                return true;
            }
            sender.sendMessage(Utils.Fill.destroy(Bukkit.getWorld(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), a, s, false));
            return true;
        } else if (args[0].equalsIgnoreCase("fill")) {
            List<Material> a = new ArrayList<>();
            a.add(Material.BEDROCK);
            if (sender instanceof Player) {
                if (((Player) sender).getGameMode() != GameMode.CREATIVE) {
                    sender.sendMessage(Utils.Fill.fill_item(Bukkit.getWorld(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),a,Material.valueOf(args[8]),(Player)sender));
                    return true;
                }
            }
            sender.sendMessage(Utils.Fill.fill(Bukkit.getWorld(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),a,Material.valueOf(args[8])));
        } else if (args[0].equalsIgnoreCase("wall_fill")) {
            List<Material> a = new ArrayList<>();
            a.add(Material.BEDROCK);
            sender.sendMessage(Utils.Fill.wall_fill_item(Bukkit.getWorld(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),a,Material.valueOf(args[8]),(Player)sender));
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reload")) {
            ls.add("reload");
        }
        if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reloadserver")) {
            ls.add("reloadserver");
        }
        if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reloaddata")) {
            ls.add("reloaddata");
        }
        Collections.sort(ls);
        return ls;
    }
}
