package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Group;
import org.functions.Bukkit.Main.functions.PermissionsUtils;
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
        if (args.length == 2) {
            sender.sendMessage(fpi.subcmd());
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.set")) {
                return true;
            }
            for (Group g : fpi.getInstance().getPlayerManager().getGroups()) {
                if (g.getGroupName().equalsIgnoreCase(args[2])) {
                    user.setGroup(g.getGroupName());
                    sender.sendMessage(fpi.putLanguage("SetUserGroup","成功将 %player% 移动到 %group%",null,new String[]{"%player%","%group%"},new String[]{user.getOfflinePlayer().getName(),g.getGroupName()}));
                    return true;
                }
            }
        }
        if (args[1].equalsIgnoreCase("permissions")) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.permissions")) {
                return true;
            }
            if ("add".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.permissions.add")) {
                    return true;
                }
                if (user.addPermissions(args[3])) {
                    sender.sendMessage(fpi.putLanguage("AddPermissionToUser", "&a成功将 %permission% 添加到 %user%", null, new String[]{"%permission%", "%user%"}, new String[]{args[3], user.getOfflinePlayer().getName()}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedPermissionToUser", "&c用户 %user% 的 %permission% 是否存在？", null, new String[]{"%permission%", "%user%"}, new String[]{args[3], user.getOfflinePlayer().getName()}));
                return true;
            }
            if ("remove".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.permissions.remove")) {
                    return true;
                }
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
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefix")) {
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
            user.setPrefix(t);
            sender.sendMessage(fpi.putLanguage("SetUserPrefix","&a成功将 %user% 的头街设置成 \"%prefix%\"",null,new String[]{"%user%","%prefix%"},new String[]{user.getOfflinePlayer().getName(),t}));
            return true;
        }
        if ("suffix".equalsIgnoreCase(args[0])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffix")) {
                return true;
            }
            String t = args[2];
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i <= args.length; i++) {
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
            user.setSuffix(t);
            sender.sendMessage(fpi.putLanguage("SetUserSuffix","&a成功将 %user% 的尾街设置成 \"%suffix%\"",null,new String[]{"%user%","%suffix%"},new String[]{user.getOfflinePlayer().getName(),t}));
            return true;
        }
        if ("prefixes".equalsIgnoreCase(args[1])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes")) {
                return true;
            }
            if (args.length <= 2) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= user.getPrefixes().size(); i++) {
                    String prefix = user.getPrefixes().get(i-1);
                    if (i != user.getPrefixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("PrefixesListUser","用户 %user% 的头街(%size%): %prefixes%",null,new Object[]{"user",user.getOfflinePlayer().getName(),"size",user.getPrefixes().size(),"prefixes",list.toString()}));
                return true;
            }
            if ("add".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.add")) {
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
                if (user.addPrefixes(t)) {
                    sender.sendMessage(fpi.putLanguage("AddUserPrefixes","&a成功将头街 \"%prefix%\" 添加在 %user% 上！",null,new String[]{"%user%","%prefix%"},new String[]{user.getName(),t}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedUserPrefixes","&c头街 \"%prefix%\" 是否在 %user% 上？",null,new String[]{"%user%","%prefix%"},new String[]{user.getName(),t}));
                return true;
            }
            if ("list".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= user.getPrefixes().size(); i++) {
                    String prefix = user.getPrefixes().get(i-1);
                    if (i != user.getPrefixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("PrefixesListUser","用户 %user% 的头街(%size%): %prefixes%",null,new Object[]{"user",user.getOfflinePlayer().getName(),"size",user.getPrefixes().size(),"prefixes",list.toString()}));
                return true;
            }

            if ("remove".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.remove")) {
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
                if (user.getPrefixes().size() <= i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigUserPrefixes","&c用户 %user% 的头街组数量 %size% 大于或等于 %number%",null,new String[]{"%user%","%number%","%size%"},new String[]{user.getName(),i+"",user.getPrefixes().size()+""}));
                    return true;
                }
                //System.out.println(i);
                //System.out.println(user.getPrefixes().size());
                String s = user.removePrefixes(i);
                if (s != null) {
                    sender.sendMessage(fpi.putLanguage("RemovePrefixesUser","&a用户 %user% 的头街 \"%prefix%\" 成功移除",null,new Object[]{"user",user.getOfflinePlayer().getName(),"prefix",s}));
                    return true;
                }
            }

            if ("use".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.use")) {
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
                if (user.getPrefixes().size() < i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigUserPrefixes","&c用户 %user% 的头街组数量 %size% 大于 %number%",null,new String[]{"%user%","%number%","%size%"},new String[]{user.getName(),i+"",user.getPrefixes().size()+""}));
                    return true;
                }
                user.setPrefix(user.getPrefixes().get(i));
                sender.sendMessage(fpi.putLanguage("ChangePrefixesUser","&a成功切换 %user% 的 \"%prefix%\" 头街",null,new String[]{"%user%",user.getName(),"%prefix%", user.getPrefix()}));
                return true;
            }
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.list")) {
                return true;
            }
            String format = "%1$s:\"%2$s\"";
            StringBuilder list = new StringBuilder();
            for (int i = 1; i <= user.getPrefixes().size(); i++) {
                String prefix = user.getPrefixes().get(i-1);
                if (i != user.getPrefixes().size()) {
                    list.append(String.format(format, i-1, prefix)).append(", ");
                    //System.out.println(list.toString());
                    continue;
                }
                list.append(String.format(format, i-1, prefix));
                //System.out.println(list.toString());
            }
            sender.sendMessage(fpi.putLanguage("PrefixesListUser","用户 %user% 的头街(%size%): %prefixes%",null,new Object[]{"user",user.getOfflinePlayer().getName(),"size",user.getPrefixes().size(),"prefixes",list.toString()}));
            return true;
        }
        if ("suffixes".equalsIgnoreCase(args[1])) {
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes")) {
                return true;
            }
            if (args.length <= 2) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= user.getSuffixes().size(); i++) {
                    String prefix = user.getSuffixes().get(i-1);
                    if (i != user.getSuffixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("SuffixesListUser","用户 %user% 的尾街(%size%): %suffixes%",null,new Object[]{"user",user.getOfflinePlayer().getName(),"size",user.getSuffixes().size(),"suffixes",list.toString()}));
                return true;
            }
            if ("add".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.add")) {
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
                if (user.addSuffixes(t)) {
                    sender.sendMessage(fpi.putLanguage("AddUserSuffixes","&a成功将尾街 \"%suffix%\" 添加在 %user% 上！",null,new String[]{"%user%","%suffix%"},new String[]{user.getName(),t}));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AddFailedUserSuffixes","&c尾街 \"%suffix%\" 是否在 %user% 上？",null,new String[]{"%user%","%suffix%"},new String[]{user.getName(),t}));
                return true;
            }
            if ("list".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.list")) {
                    return true;
                }
                String format = "%1$s:\"%2$s\"";
                StringBuilder list = new StringBuilder();
                for (int i = 1; i <= user.getSuffixes().size(); i++) {
                    String prefix = user.getSuffixes().get(i-1);
                    if (i != user.getSuffixes().size()) {
                        list.append(String.format(format, i-1, prefix)).append(", ");
                        //System.out.println(list.toString());
                        continue;
                    }
                    list.append(String.format(format, i-1, prefix));
                    //System.out.println(list.toString());
                }
                sender.sendMessage(fpi.putLanguage("SuffixesListUser","用户 %user% 的尾街(%size%): %suffixes%",null,new Object[]{"user",user.getOfflinePlayer().getName(),"size",user.getSuffixes().size(),"suffixes",list.toString()}));
                return true;
            }

            if ("remove".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.remove")) {
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
                if (user.getSuffixes().size() <= i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigUserSuffixes","&c用户 %user% 的尾街组数量 %size% 大于或等于 %number%",null,new String[]{"%user%","%number%","%size%"},new String[]{user.getName(),i+"",user.getPrefixes().size()+""}));
                    return true;
                }
                String s = user.removeSuffixes(i);
                if (s != null) {
                    sender.sendMessage(fpi.putLanguage("RemoveSuffixesUser","&a用户 %user% 的尾街 \"%suffix%\" 成功移除",null,new Object[]{"user",user.getOfflinePlayer().getName(),"suffix",s}));
                    return true;
                }
            }
            if ("use".equalsIgnoreCase(args[2])) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.use")) {
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
                if (user.getSuffixes().size() < i) {
                    sender.sendMessage(fpi.putLanguage("NumberBigUserSuffixes","&c用户 %user% 的尾街组数量 %size% 大于 %number%",null,new String[]{"%user%","%number%","%size%"},new String[]{user.getName(),i+"",user.getPrefixes().size()+""}));
                    return true;
                }
                user.setSuffix(user.getPrefixes().get(i));
                sender.sendMessage(fpi.putLanguage("ChangeSuffixesUser","&a成功切换 %user% 的 \"%suffix%\" 头街",null,new String[]{"%user%",user.getName(),"%suffix%", user.getSuffix()}));
                return true;
            }
            if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.list")) {
                return true;
            }
            String format = "%1$s:\"%2$s\"";
            StringBuilder list = new StringBuilder();
            for (int i = 1; i <= user.getSuffixes().size(); i++) {
                String prefix = user.getSuffixes().get(i-1);
                if (i != user.getSuffixes().size()) {
                    list.append(String.format(format, i-1, prefix)).append(", ");
                    //System.out.println(list.toString());
                    continue;
                }
                list.append(String.format(format, i-1, prefix));
                //System.out.println(list.toString());
            }
            sender.sendMessage(fpi.putLanguage("SuffixesListUser","用户 %user% 的尾街(%size%): %suffixes%",null,new Object[]{"user",user.getOfflinePlayer().getName(),"size",user.getSuffixes().size(),"suffixes",list.toString()}));
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
            if (args[0] != null) {
                ls.clear();
                Functions.instance.getPlayerManager().getAllUser().forEach(e-> {
                    if (e.getOfflinePlayer().getName().toLowerCase().startsWith(args[0].toLowerCase())) ls.add(e.getOfflinePlayer().getName());
                });
            }
        }
        if (args.length == 2) {
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.set")) ls.add("set");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefix")) ls.add("prefix");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffix")) ls.add("suffix");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes")) ls.add("suffixes");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes")) ls.add("prefixes");
        }
        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("suffixes")) {
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.remove")) ls.add("remove");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.use")) ls.add("use");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.add")) ls.add("add");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.suffixes.list")) ls.add("list");
            }
            if (args[1].equalsIgnoreCase("prefixes")) {
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.remove")) ls.add("remove");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.use")) ls.add("use");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.add")) ls.add("add");
                if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.user.prefixes.list")) ls.add("list");
            }
        }
        Collections.sort(ls);
        return ls;
    }
}
