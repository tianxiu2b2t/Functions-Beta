package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCordTeleport;
import org.functions.Bukkit.Main.functions.Messaging.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandServer implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("server", new CommandServer());
    }

    FPI fpi = Functions.instance.getAPI();

    public BungeeCordTeleport getTeleport() {
        return (BungeeCordTeleport) Manager.manager.getClass("BungeeCordBetweenServers");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Functions.instance.getFServer().isBc()) {
            getTeleport().getGetBcServer().SendBCDataGetServerList();
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (args.length == 1) {
                    getTeleport().getGetBcServer().ServerList.forEach((e) -> {
                        if (args[0].equalsIgnoreCase(e)) {
                            getTeleport().getBcTeleport().Run(player, e);
                        }
                    });
                }
                if (args.length == 2) {
                    getTeleport().getGetBcServer().ServerList.forEach((e) -> {
                        if (args[0].equalsIgnoreCase(e)) {
                            getTeleport().getGetBcServer().SendBCDataGetServerOnlinePlayers();
                            getTeleport().getGetBcServer().OnlinePlayers.forEach((Player, ServerName) -> {
                                if (!ServerName.equalsIgnoreCase(e)) {
                                    if (args[1].equalsIgnoreCase(Player)) {
                                        getTeleport().getBcTeleport().Run(Player, e);
                                    }
                                }
                            });
                        }
                    });
                }
            } else {
                sender.sendMessage(fpi.noPlayer());
            }
        } else {
            sender.sendMessage(fpi.putLanguage("OnlyBungeeCordUseIt","&c这条指令你不能执行，因为需要BungeeCord的支持才能执行",null));
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (Functions.instance.getFServer().isBc()) {
            if (args.length <= 1) {
                getTeleport().getGetBcServer().SendBCDataGetServerList();
                ls.addAll(getTeleport().getGetBcServer().ServerList);
            }
            if (args.length == 2) {
                getTeleport().getGetBcServer().SendBCDataGetServerList();
                getTeleport().getGetBcServer().ServerList.forEach((e) -> {
                    if (args[0].equalsIgnoreCase(e)) {
                        getTeleport().getGetBcServer().SendBCDataGetServerOnlinePlayers();
                        getTeleport().getGetBcServer().OnlinePlayers.forEach((Player, ServerName) -> {
                            if (!ServerName.equalsIgnoreCase(e)) {
                                ls.add(Player);
                            }
                        });
                    }
                });
            }
        } else {
            sender.sendMessage(fpi.putLanguage("OnlyBungeeCordUseIt","&c这条指令你不能执行，因为需要BungeeCord的支持才能执行",null));
        }
        Collections.sort(ls);
        return ls;
    }
}
