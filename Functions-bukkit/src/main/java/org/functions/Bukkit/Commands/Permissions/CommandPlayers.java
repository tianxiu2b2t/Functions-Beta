package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.API.WorldBlock;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.IUser;
import org.functions.Bukkit.Main.functions.PermissionsUtils;
import org.functions.Bukkit.Main.functions.User;
import org.functions.Bukkit.Main.functions.Utils;

import java.util.List;

public class CommandPlayers implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand(new String[]{"fly","spawn","show","invisibility"}, new CommandPlayers());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (fpi.hasAliases("fly",label)) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.fly")) {
                    return true;
                }
                p.setAllowFlight(!p.getAllowFlight());
                sender.sendMessage(fpi.putLanguage("PlayerFly","%player% 的飞行模式为 %enable%.",p,new Object[]{"player",p.getName(),"enable",fpi.changeBooleanToText(p.getAllowFlight())}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
        } else if (fpi.hasAliases("spawn",label)) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.spawn")) {
                    return true;
                }
                Location position = p.getWorld().getSpawnLocation();
                WorldBlock worldBlock = new WorldBlock(position);
                position.setY(worldBlock.onGroundY());
                p.teleport(position);
                sender.sendMessage(fpi.putLanguage("PlayerTeleportToSpawn","%player% 已传送至 %position%.",p,new Object[]{"player",p.getName(),"position",fpi.changeLocationToString(position)}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
        } else if (fpi.hasAliases("invisibility",label)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.invisibility")) {
                        return true;
                    }
                    IUser user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
                    user.setInvisibility(true);
                    sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", p, new Object[]{"player", p.getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
                    return true;
                }
            }
            User user = Functions.instance.getPlayerManager().getUser(args[0],true);
            if (user == null) {
                sender.sendMessage(Functions.instance.getPlayerManager().NotFound(args[0]));
                return true;
            }
            user.setInvisibility(true);
            sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", null, new Object[]{"player", user.getOfflinePlayer().getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
            return true;
        } else if (fpi.hasAliases("show",label)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.invisibility")) {
                        return true;
                    }
                    IUser user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
                    user.setInvisibility(false);
                    sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", p, new Object[]{"player", p.getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
                    return true;
                }
            }
            User user = Functions.instance.getPlayerManager().getUser(args[0],true);
            if (user == null) {
                sender.sendMessage(Functions.instance.getPlayerManager().NotFound(args[0]));
                return true;
            }
            user.setInvisibility(false);
            sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", null, new Object[]{"player", user.getOfflinePlayer().getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
            return true;
        } else if (fpi.hasAliases("ping",label)) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.ping")) {
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("PlayerPing","%player% 的延迟为 %ping%ms",p,new Object[]{"player",p.getName(),"ping", new Utils.Ping(p).toString()}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
