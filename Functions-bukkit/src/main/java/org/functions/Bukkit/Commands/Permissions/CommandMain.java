package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Commands.Defaults.CommandRegister;
import org.functions.Bukkit.Main.Economy;
import org.functions.Bukkit.Main.Functions;

import java.util.List;

public class CommandMain implements TabExecutor {
    public void run() {
        new FPI().getCommand("functions", new CommandMain());
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        long start = 0;
        if (args.length == 0) {
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            start = System.currentTimeMillis();
            Functions.instance.getConfiguration().reload();
            sender.sendMessage(Functions.instance.getAPI().putLanguage("ReloadConfiguration","&a成功重载配置！ (%time-ms% ms)").replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
            return true;
        }
        if (args[0].equalsIgnoreCase("reloadServer")) {
            Functions.instance.getServer().reload();
            start = System.currentTimeMillis();
            sender.sendMessage(Functions.instance.getAPI().putLanguage("ReloadServer","&a成功重载服务器！ (%time-ms% ms)").replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
            return true;
        }
        if (args[0].equalsIgnoreCase("reloadData")) {
            start = System.currentTimeMillis();
            Functions.instance.getServer().reloadData();
            sender.sendMessage(Functions.instance.getAPI().putLanguage("ReloadMinecraftData","&a成功重载原版数据！ (%time-ms% ms)").replace("%time-ms%", (((double)System.currentTimeMillis() - start) / 1000) + ""));
            return true;
        }
        Player p = ((Player)sender).getPlayer();
        Economy economy = new Economy(p.getUniqueId());
        if (args[0].equalsIgnoreCase("add")) {
            sender.sendMessage(economy.display());
            economy.addBalance(5.0);
            sender.sendMessage(economy.display());
        }
        if (args[0].equalsIgnoreCase("display")) {
            sender.sendMessage(economy.display());
        }
        if (args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage(economy.display());
            economy.takeBalance(5.0);
            sender.sendMessage(economy.display());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
