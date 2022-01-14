package org.functions.Bukkit.Commands;

import org.bukkit.command.TabExecutor;
import org.functions.Bukkit.Main.Functions;

public class ICommandRegister {
    public ICommandRegister(String[] cmd_args, TabExecutor executor) {
        Functions.instance.getAPI().getCommand(cmd_args, executor);
    }
    public ICommandRegister(String cmd, TabExecutor executor) {
        this(new String[]{cmd},executor);
    }
}
