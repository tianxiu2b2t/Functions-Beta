package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.PermissionsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGroupanti implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("group", new CommandGroupanti());
    }
    FPI fpi = Functions.instance.getAPI();
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(fpi.subcmd());
            return true;
        }
            if ("list".equalsIgnoreCase(args[0])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.list")) {
                    return true;
                }
                String format = "%1$s(%2$s)";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= Functions.instance.getPlayerManager().getGroups().size();i++) {
                    Group g = Functions.instance.getPlayerManager().getGroups().get(i-1);
                    if (i != Functions.instance.getPlayerManager().getGroups().size()) {
                        list.append(format.replace("%1$s",g.getGroupName()).replace("%2$s",i+"")).append(", ");
                        System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, g.getGroupName(), i));
                    System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("ListGroupName","组(%group_size%): %group_list%",null,new String[]{"%group_size%","%group_list%"},new String[]{fpi.getInstance().getPlayerManager().getGroups().size()+"",list.toString()}));
                return true;
            }
//            if ("set".equalsIgnoreCase(args[0])) {
//                for (User e : fpi.getInstance().getPlayerManager().getAllUser()) {
//                    if (e.getOfflinePlayer().getName().equalsIgnoreCase(args[1])) {
//                        for (Group g : fpi.getInstance().getPlayerManager().getGroups()) {
//                            if (g.getGroupName().equalsIgnoreCase(args[2])) {
//                                e.setGroup(g.getGroupName());
//                                sender.sendMessage(fpi.putLanguage("SetGroup","成功将 %player% 移动到 %group%",null,new String[]{"%player%","%group%"},new String[]{e.getOfflinePlayer().getName(),g.getGroupName()}));
//                                return true;
//                            }
//                        }
//                    }
//                }
        if ("permissions".equalsIgnoreCase(args[0])) {
            Group group = null;
            for (Group g : fpi.getInstance().getPlayerManager().getGroups()) {
                if (g.getGroupName().equalsIgnoreCase(args[1])) {
                    group = g;
                }
            }
            if (group == null) {
                sender.sendMessage(fpi.putLanguage("NotFindGroup","&c无法找到 %group% 用户组！",null,new String[]{"%group%"},new String[]{args[0]}));
                return true;
            }
            if ("add".equalsIgnoreCase(args[2])) {
                if (group.addAllPermissions(args[3])) {
                    sender.sendMessage(fpi.putLanguage("AddPermissionToGroup","&a成功将 %permission% 添加到 %group%",null,new String[]{"%permission%","%group%"},new String[]{args[3],group.getGroupName()}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedPermissionToGroup","&c用户组 %group% 的 %permission% 是否存在？",null,new String[]{"%permission%","%group%"},new String[]{args[3],group.getGroupName()}));
                return true;
            }
            if ("remove".equalsIgnoreCase(args[2])) {
                if (group.removeAllPermissions(args[3])) {
                    sender.sendMessage(fpi.putLanguage("RemovePermissionToGroup","&a成功将 %group% 的 %permission% 移除！",null,new String[]{"%permission%","%group%"},new String[]{args[3],group.getGroupName()}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("RemoveFailedPermissionToGroup","&c用户组 %group% 的 %permission% 是否存在？",null,new String[]{"%permission%","%group%"},new String[]{args[3],group.getGroupName()}));
                return true;
            }
        }
        if ("prefix".equalsIgnoreCase(args[0])) {
            return true;
        }
        if ("suffix".equalsIgnoreCase(args[0])) {
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
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.list")) {
                ls.add("list");
            }
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions")) {
                ls.add("permissions");
            }
        }
        if (args.length == 2) {
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.list")) {
                Functions.instance.getPlayerManager().getGroups().forEach(e->{ ls.add(e.getGroupName());});
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("permissions")) {
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions.remove")) ls.add("remove");
            }
        }
        if (args.length == 4) {
            if (args[2].equalsIgnoreCase("remove")) {
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions.remove")) {
                    Functions.instance.getPlayerManager().getGroups().forEach(e->{
                        if (e.getGroupName().equalsIgnoreCase(args[1])) {
                            ls.addAll(e.getAllPermissions());
                        }
                    });
                }
            }
        }
        Collections.sort(ls);
        return ls;
    }
}
