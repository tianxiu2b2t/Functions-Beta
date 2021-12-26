package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandServer implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("server", new CommandServer());
    }

    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Functions.instance.getFServer().isBc()) {
            Functions.instance.getMessaging().getGetBcServer().SendBCDataGetServerList();
            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (args.length == 1) {
                    Functions.instance.getMessaging().getGetBcServer().ServerList.forEach((e) -> {
                        if (args[0].equalsIgnoreCase(e)) {
                            Functions.instance.getMessaging().getBcTeleport().Run(player, e);
                        }
                    });
                }
                if (args.length == 2) {
                    Functions.instance.getMessaging().getGetBcServer().ServerList.forEach((e) -> {
                        if (args[0].equalsIgnoreCase(e)) {
                            Functions.instance.getMessaging().getGetBcServer().SendBCDataGetServerOnlinePlayers();
                            Functions.instance.getMessaging().getGetBcServer().OnlinePlayers.forEach((Player, ServerName) -> {
                                if (!ServerName.equalsIgnoreCase(e)) {
                                    if (args[1].equalsIgnoreCase(Player)) {
                                        Functions.instance.getMessaging().getBcTeleport().Run(Player, e);
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
                Functions.instance.getMessaging().getGetBcServer().SendBCDataGetServerList();
                ls.addAll(Functions.instance.getMessaging().getGetBcServer().ServerList);
            }
            if (args.length == 2) {
                Functions.instance.getMessaging().getGetBcServer().SendBCDataGetServerList();
                Functions.instance.getMessaging().getGetBcServer().ServerList.forEach((e) -> {
                    if (args[0].equalsIgnoreCase(e)) {
                        Functions.instance.getMessaging().getGetBcServer().SendBCDataGetServerOnlinePlayers();
                        Functions.instance.getMessaging().getGetBcServer().OnlinePlayers.forEach((Player, ServerName) -> {
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
