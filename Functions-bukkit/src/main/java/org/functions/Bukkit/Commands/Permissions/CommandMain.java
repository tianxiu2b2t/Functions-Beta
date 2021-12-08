package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.functions.Bank;
import org.functions.Bukkit.Main.functions.Economy;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.PermissionsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
            sender.sendMessage("&a成功重载服务器！ (%time-ms% ms)".replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
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
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reload")) {
            ls.add("reload");
        }
        if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reloadserver")) {
            ls.add("reloadserver");
        }
        if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.command.permissions.main.reloaddata")) {
            ls.add("reloaddata");
        }
        Collections.sort(ls);
        return ls;
    }
}
