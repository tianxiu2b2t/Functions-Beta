package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUser implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("user", new CommandUser());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = null;
        for (User u : Functions.instance.getPlayerManager().getAllUser()) {
            if (args[0].equalsIgnoreCase(u.getOfflinePlayer().getName())) {
                user = u;
            }
        }
        if (user == null) {
            sender.sendMessage(fpi.putLanguage("NotFindUser","&c找不到 %user% !",null,new String[]{"%user%"},new String[]{args[0]}));
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            for (Group g : fpi.getInstance().getPlayerManager().getGroups()) {
                if (g.getGroupName().equalsIgnoreCase(args[2])) {
                    user.setGroup(g.getGroupName());
                    sender.sendMessage(fpi.putLanguage("SetUserGroup","成功将 %player% 移动到 %group%",null,new String[]{"%player%","%group%"},new String[]{user.getOfflinePlayer().getName(),g.getGroupName()}));
                    return true;
                }
            }
        }
        if (args[1].equalsIgnoreCase("permissions")) {
            if ("add".equalsIgnoreCase(args[2])) {
                if (user.addPermissions(args[3])) {
                    sender.sendMessage(fpi.putLanguage("AddPermissionToUser", "&a成功将 %permission% 添加到 %user%", null, new String[]{"%permission%", "%user%"}, new String[]{args[3], user.getOfflinePlayer().getName()}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedPermissionToUser", "&c用户 %user% 的 %permission% 是否存在？", null, new String[]{"%permission%", "%user%"}, new String[]{args[3], user.getOfflinePlayer().getName()}));
                return true;
            }
            if ("remove".equalsIgnoreCase(args[2])) {
                if (user.removePermissions(args[3])) {
                    sender.sendMessage(fpi.putLanguage("RemovePermissionToUser", "&a成功将 %user% 的 %permission% 移除！", null, new String[]{"%permission%", "%user%"}, new String[]{args[3], user.getOfflinePlayer().getName()}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("RemoveFailedPermissionToUser", "&c用户 %user% 的 %permission% 是否存在？", null, new String[]{"%permission%", "%user%"}, new String[]{args[3], user.getOfflinePlayer().getName()}));
                return true;
            }
            return true;
        }
        if ("prefix".equalsIgnoreCase(args[1])) {
            String t = args[2];
            StringBuilder sb = new StringBuilder();
            for (int i = 3; i <= args.length; i++) {
                if (args.length != i) {
                    sb.append(args[i-1]).append(" ");
                    continue;
                }
                sb.append(args[i-1]);
            }
            t = sb.toString();
            if (t.startsWith("'")) {
                t = t.substring(1);
            }
            if (t.endsWith("'")) {
                t = t.substring(0,t.length() - 1);
            }
            user.setPrefix(t);
            sender.sendMessage(fpi.putLanguage("SetUserPrefix","&a成功将 %user% 的头街设置成 \"%prefix%\"",null,new String[]{"%user%","%prefix%"},new String[]{user.getOfflinePlayer().getName(),t}));
            return true;
        }
        if ("suffix".equalsIgnoreCase(args[0])) {
            Group group = null;
            for (Group g : fpi.getInstance().getPlayerManager().getGroups()) {
                if (g.getGroupName().equalsIgnoreCase(args[1])) {
                    group = g;
                }
            }
            if (group == null) {
                sender.sendMessage(fpi.putLanguage("NotFindGroup", "&c无法找到 %group% 用户组！", null, new String[]{"%group%"}, new String[]{args[0]}));
                return true;
            }
            String t = args[2];
            StringBuilder sb = new StringBuilder();
            for (int i = 3; i <= args.length; i++) {
                if (args.length != i) {
                    sb.append(args[i-1]).append(" ");
                    continue;
                }
                sb.append(args[i-1]);
            }
            t = sb.toString();
            if (t.startsWith("'")) {
                t = t.substring(1);
            }
            if (t.endsWith("'")) {
                t = t.substring(0,t.length() - 1);
            }
            group.setSuffix(t);
            sender.sendMessage(fpi.putLanguage("SetGroupSuffix","&a成功将 %group% 的尾街设置成 \"%suffix%\"",null,new String[]{"%group%","%suffix%"},new String[]{group.getGroupName(),t}));
            return true;
        }
        if ("prefixes".equalsIgnoreCase(args[0])) {
            return true;
        }
        if ("suffixes".equalsIgnoreCase(args[0])) {
            return true;
        }
        sender.sendMessage(fpi.subcmd());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (args.length <= 1) {
            Functions.instance.getPlayerManager().getAllUser().forEach(e-> {
               ls.add(e.getOfflinePlayer().getName());
            });
        }
        Collections.sort(ls);
        return ls;
    }
}
