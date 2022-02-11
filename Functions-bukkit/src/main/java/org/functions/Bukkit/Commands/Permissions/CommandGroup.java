package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.PermissionsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGroup implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("group", new CommandGroup());
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
                        list.append(String.format(format, g.getGroupName(), i)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, g.getGroupName(), i));
                    //System.out.println(list.toString());
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
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions")) {
                    return true;
                }
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
                if (args.length == 2) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.list")) {
                        return true;
                    }
                    String format = "%1$s(%2$s)";
                    StringBuilder list = new StringBuilder();
                    for (int i = 1; i <= group.getAllPermissions().size();i++) {
                        if (i != group.getAllPermissions().size()) {
                            list.append(String.format(format, group.getAllPermissions().get(i-1))).append(", ");
                            //System.out.println(list.toString());
                            continue;
                        }
                        list.append(String.format(format, group.getAllPermissions().get(i-1), i));
                        //System.out.println(list.toString());
                    }
                    sender.sendMessage(fpi.putLanguage("GroupPermissionsList","&a用户组 %group% 的权限有: %permissions",null,new Object[]{"group",group.getGroupName(),"permissions",list.toString()}));
                    return true;
                }
                if ("list".equalsIgnoreCase(args[2])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.list")) {
                        return true;
                    }
                    String format = "%1$s(%2$s)";
                    StringBuilder list = new StringBuilder();
                    for (int i = 1; i <= group.getAllPermissions().size();i++) {
                        String g = group.getAllPermissions().get(i-1);
                        if (i != group.getAllPermissions().size()) {
                            list.append(String.format(format, group.getAllPermissions().get(i-1))).append(", ");
                            //System.out.println(list.toString());
                            continue;
                        }
                        list.append(String.format(format, group.getAllPermissions().get(i-1), i));
                        //System.out.println(list.toString());
                    }
                    sender.sendMessage(fpi.putLanguage("GroupPermissionsList","&a用户组 %group% 的权限有: %permissions",null,new Object[]{"group",group.getGroupName(),"permissions",list.toString()}));
                    return true;
                }
                if ("add".equalsIgnoreCase(args[2])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions.add")) {
                        return true;
                    }
                    if (group.addAllPermissions(args[3])) {
                        sender.sendMessage(fpi.putLanguage("AddPermissionToGroup", "&a成功将 %permission% 添加到 %group%", null, new String[]{"%permission%", "%group%"}, new String[]{args[3], group.getGroupName()}));
                        return true;
                    }
                    sender.sendMessage(fpi.putLanguage("AddFailedPermissionToGroup", "&c用户组 %group% 的 %permission% 是否存在？", null, new String[]{"%permission%", "%group%"}, new String[]{args[3], group.getGroupName()}));
                    return true;
                }
                if ("remove".equalsIgnoreCase(args[2])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions.remove")) {
                        return true;
                    }
                    if (group.removeAllPermissions(args[3])) {
                        sender.sendMessage(fpi.putLanguage("RemovePermissionToGroup", "&a成功将 %group% 的 %permission% 移除！", null, new String[]{"%permission%", "%group%"}, new String[]{args[3], group.getGroupName()}));
                        return true;
                    }
                    sender.sendMessage(fpi.putLanguage("RemoveFailedPermissionToGroup", "&c用户组 %group% 的 %permission% 是否存在？", null, new String[]{"%permission%", "%group%"}, new String[]{args[3], group.getGroupName()}));
                    return true;
                }
        }
        if ("prefix".equalsIgnoreCase(args[0])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefix")) {
                return true;
            }
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
            if (t.startsWith("'") || t.startsWith("\"")) {
                t = t.substring(1);
            }
            if (t.endsWith("'") || t.endsWith("\"")) {
                t = t.substring(0,t.length() - 1);
            }
            group.setPrefix(t);
            sender.sendMessage(fpi.putLanguage("SetGroupPrefix","&a成功将 %group% 的头街设置成 \"%prefix%\"",null,new String[]{"%group%","%prefix%"},new String[]{group.getGroupName(),t}));
            return true;
        }
        if ("suffix".equalsIgnoreCase(args[0])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffix")) {
                return true;
            }
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
            if (t.startsWith("'") || t.startsWith("\"")) {
                t = t.substring(1);
            }
            if (t.endsWith("'") || t.endsWith("\"")) {
                t = t.substring(0,t.length() - 1);
            }
            group.setSuffix(t);
            sender.sendMessage(fpi.putLanguage("SetGroupSuffix","&a成功将 %group% 的尾街设置成 \"%suffix%\"",null,new String[]{"%group%","%suffix%"},new String[]{group.getGroupName(),t}));
            return true;
        }
        if ("prefixes".equalsIgnoreCase(args[0])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes")) {
                return true;
            }
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
            if (args.length <= 2) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= group.getPrefixes().size(); i++) {
                    String prefix = group.getPrefixes().get(i-1);
                    if (i != group.getPrefixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("PrefixesListGroup","用户组 %group% 的头街(%size%): %prefixes%",null,new Object[]{"group",group.getGroupName(),"size",group.getPrefixes().size(),"prefixes",list.toString()}));
                return true;
            }
            if ("add".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.add")) {
                    return true;
                }
                String t = args[3];
                StringBuilder sb = new StringBuilder();
                for (int i = 4; i <= args.length; i++) {
                    if (args.length != i) {
                        sb.append(args[i-1]).append(" ");
                        continue;
                    }
                    sb.append(args[i-1]);
                }
                t = sb.toString();
                if (t.startsWith("'") || t.startsWith("\"")) {
                    t = t.substring(1);
                }
                if (t.endsWith("'") || t.endsWith("\"")) {
                    t = t.substring(0,t.length() - 1);
                }
                if (group.addPrefixes(t)) {
                    sender.sendMessage(fpi.putLanguage("AddGroupPrefixes","&a成功将头街 \"%prefix%\" 添加在 %group% 上！",null,new String[]{"%group%","%prefix%"},new String[]{group.getGroupName(),t}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedGroupPrefixes","&c头街 \"%prefix%\" 是否在 %group% 上？",null,new String[]{"%group%","%prefix%"},new String[]{group.getGroupName(),t}));
                return true;
            }
            if ("list".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= group.getPrefixes().size(); i++) {
                    String prefix = group.getPrefixes().get(i-1);
                    if (i != group.getPrefixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("PrefixesListGroup","用户组 %group% 的头街(%size%): %prefixes%",null,new Object[]{"group",group.getGroupName(),"size",group.getPrefixes().size(),"prefixes",list.toString()}));
                return true;
            }

            if ("remove".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.remove")) {
                    return true;
                }
                int i = 0;
                try {
                    i = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatWrong","&c%string% 不是数字",null,new String[]{"%string%"},new String[]{args[3]}));
                    e.fillInStackTrace();
                    return true;
                }
                if (i <= -1) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatTooSmall","&c%string% 的数字太小啦",null,new String[]{"%string%"},new String[]{args[3]}));
                    return true;
                }
                if (group.getPrefixes().size() <= i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigGroupPrefixes","&c用户组 %group% 的头街组数量 %size% 大于或等于 %number%",null,new String[]{"%group%","%number%","%size%"},new String[]{group.getGroupName(),i+"",group.getPrefixes().size()+""}));
                    return true;
                }
                //System.out.println(i);
                //System.out.println(group.getPrefixes().size());
                String s = group.removePrefixes(i);
                if (s != null) {
                    sender.sendMessage(fpi.putLanguage("RemovePrefixesGroup","&a用户组 %group% 的头街 \"%prefix%\" 成功移除",null,new Object[]{"group",group.getGroupName(),"prefix",s}));
                    return true;
                }
            }
            if ("use".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.use")) {
                    return true;
                }
                int i = 0;
                try {
                    i = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatWrong","&c%string% 不是数字",null,new String[]{"%string%"},new String[]{args[3]}));
                    e.fillInStackTrace();
                    return true;
                }
                if (i <= -1) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatTooSmall","&c%string% 的数字太小啦",null,new String[]{"%string%"},new String[]{args[3]}));
                    return true;
                }
                if (group.getPrefixes().size() < i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigGroupPrefixes","&c用户组 %group% 的头街组数量 %size% 大于 %number%",null,new String[]{"%group%","%number%","%size%"},new String[]{group.getGroupName(),i+"",group.getPrefixes().size()+""}));
                    return true;
                }
                group.setPrefix(group.getPrefixes().get(i));
                sender.sendMessage(fpi.putLanguage("ChangePrefixesGroup","&a成功切换 %group% 的 \"%prefix%\" 头街",null,new String[]{"%group%",group.getGroupName(),"%prefix%", group.getPrefix()}));
                return true;
            }
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.list")) {
                return true;
            }
            String format = "%1$s:\"%2$s\"";
            StringBuilder list = new StringBuilder();
            for (int i = 1; i <= group.getPrefixes().size(); i++) {
                String prefix = group.getPrefixes().get(i-1);
                if (i != group.getPrefixes().size()) {
                    list.append(String.format(format, i-1, prefix)).append(", ");
                    //System.out.println(list.toString());
                    continue;
                }
                list.append(String.format(format, i-1, prefix));
                //System.out.println(list.toString());
            }
            sender.sendMessage(fpi.putLanguage("PrefixesListGroup","用户组 %group% 的头街(%size%): %prefixes%",null,new Object[]{"group",group.getGroupName(),"size",group.getPrefixes().size(),"prefixes",list.toString()}));
            return true;
        }
        if ("suffixes".equalsIgnoreCase(args[0])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes")) {
                return true;
            }
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
            if (args.length <= 2) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= group.getSuffixes().size(); i++) {
                    String prefix = group.getSuffixes().get(i-1);
                    if (i != group.getSuffixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("SuffixesListGroup","用户组 %group% 的尾街(%size%): %suffixes%",null,new Object[]{"group",group.getGroupName(),"size",group.getSuffixes().size(),"suffixes",list.toString()}));
                return true;
            }
            if ("add".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.add")) {
                    return true;
                }
                String t = args[3];
                StringBuilder sb = new StringBuilder();
                for (int i = 4; i <= args.length; i++) {
                    if (args.length != i) {
                        sb.append(args[i-1]).append(" ");
                        continue;
                    }
                    sb.append(args[i-1]);
                }
                t = sb.toString();
                if (t.startsWith("'") || t.startsWith("\"")) {
                    t = t.substring(1);
                }
                if (t.endsWith("'") || t.endsWith("\"")) {
                    t = t.substring(0,t.length() - 1);
                }
                if (group.addSuffixes(t)) {
                    sender.sendMessage(fpi.putLanguage("AddGroupSuffixes","&a成功将尾街 \"%suffix%\" 添加在 %group% 上！",null,new String[]{"%group%","%suffix%"},new String[]{group.getGroupName(),t}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedGroupSuffixes","&c尾街 \"%suffix%\" 是否在 %group% 上？",null,new String[]{"%group%","%suffix%"},new String[]{group.getGroupName(),t}));
                return true;
            }
            if ("list".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= group.getSuffixes().size(); i++) {
                    String prefix = group.getSuffixes().get(i-1);
                    if (i != group.getSuffixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("SuffixesListGroup","用户组 %group% 的头街(%size%): %prefixes%",null,new Object[]{"group",group.getGroupName(),"size",group.getSuffixes().size(),"suffixes",list.toString()}));
                return true;
            }

            if ("remove".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.remove")) {
                    return true;
                }
                int i = 0;
                try {
                    i = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatWrong","&c%string% 不是数字",null,new String[]{"%string%"},new String[]{args[3]}));
                    e.fillInStackTrace();
                    return true;
                }
                if (i <= -1) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatTooSmall","&c%string% 的数字太小啦",null,new String[]{"%string%"},new String[]{args[3]}));
                    return true;
                }
                if (group.getPrefixes().size() <= i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigGroupSuffixes","&c用户组 %group% 的尾街组数量 %size% 大于或等于 %number%",null,new String[]{"%group%","%number%","%size%"},new String[]{group.getGroupName(),i+"",group.getPrefixes().size()+""}));
                    return true;
                }
                //System.out.println(i);
                //System.out.println(group.getPrefixes().size());
                String s = group.removePrefixes(i);
                if (s != null) {
                    sender.sendMessage(fpi.putLanguage("RemoveSuffixesGroup","&a用户组 %group% 的尾街 \"%suffix%\" 成功移除",null,new Object[]{"group",group.getGroupName(),"suffix",s}));
                    return true;
                }
            }
            if ("use".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.use")) {
                    return true;
                }
                int i = 0;
                try {
                    i = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatWrong","&c%string% 不是数字",null,new String[]{"%string%"},new String[]{args[3]}));
                    e.fillInStackTrace();
                    return true;
                }
                if (i <= -1) {
                    sender.sendMessage(fpi.putLanguage("NumberFormatTooSmall","&c%string% 的数字太小啦",null,new String[]{"%string%"},new String[]{args[3]}));
                    return true;
                }
                if (group.getPrefixes().size() < i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigGroupSuffixes","&c用户组 %group% 的尾街组数量 %size% 大于 %number%",null,new String[]{"%group%","%number%","%size%"},new String[]{group.getGroupName(),i+"",group.getPrefixes().size()+""}));
                    return true;
                }
                group.setPrefix(group.getPrefixes().get(i));
                sender.sendMessage(fpi.putLanguage("ChangeSuffixesGroup","&a成功切换 %group% 的 \"%suffix%\" 头街",null,new String[]{"%group%",group.getGroupName(),"%suffix%", group.getSuffix()}));
                return true;
            }
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.list")) {
                return true;
            }
            String format = "%1$s:\"%2$s\"";
            StringBuilder list = new StringBuilder();
            for (int i = 1; i <= group.getPrefixes().size(); i++) {
                String prefix = group.getSuffixes().get(i-1);
                if (i != group.getSuffixes().size()) {
                    list.append(String.format(format, i-1, prefix)).append(", ");
                    //System.out.println(list.toString());
                    continue;
                }
                list.append(String.format(format, i-1, prefix));
                //System.out.println(list.toString());
            }
            sender.sendMessage(fpi.putLanguage("SuffixesListGroup","用户组 %group% 的尾街(%size%): %suffixes%",null,new Object[]{"group",group.getGroupName(),"size",group.getSuffixes().size(),"suffixes",list.toString()}));
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
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffix")) {
                ls.add("suffix");
            }
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefix")) {
                ls.add("prefix");
            }
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes")) {
                ls.add("suffixes");
            }
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes")) {
                ls.add("prefixes");
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
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.permissions.add")) ls.add("add");
            }
            if (args[0].equalsIgnoreCase("suffixes")) {
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.remove")) ls.add("remove");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.use")) ls.add("use");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.add")) ls.add("add");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.suffixes.list")) ls.add("list");
            }
            if (args[0].equalsIgnoreCase("prefixes")) {
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.remove")) ls.add("remove");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.use")) ls.add("use");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.add")) ls.add("add");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.group.prefixes.list")) ls.add("list");
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("permissions")) {
                if (args[2].equalsIgnoreCase("remove")) {
                    if (PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.group.permissions.remove")) {
                        Functions.instance.getPlayerManager().getGroups().forEach(e -> {
                            if (e.getGroupName().equalsIgnoreCase(args[1])) {
                                ls.addAll(e.getAllPermissions());
                            }
                        });
                    }
                }
            }
        }
        Collections.sort(ls);
        return ls;
    }
}
