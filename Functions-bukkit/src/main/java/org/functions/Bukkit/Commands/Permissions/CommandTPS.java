package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Commands.ICommandRegister;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.ServerInfo.Info;

import java.util.List;

public class CommandTPS implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("info", new CommandTPS());
        Functions.instance.getAPI().getCommand("tps", new CommandTPS());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Info.getInfo());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
