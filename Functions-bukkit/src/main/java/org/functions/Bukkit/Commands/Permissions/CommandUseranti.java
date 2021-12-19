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

public class CommandUseranti implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("user", new CommandUseranti());
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
            return true;
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
            Functions.instance.getPlayerManager().getAllUser().forEach(e-> {
               ls.add(e.getOfflinePlayer().getName());
            });
        }
        Collections.sort(ls);
        return ls;
    }
}
