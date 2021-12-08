package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Account;
import org.functions.Bukkit.Main.functions.Accounts;
import org.functions.Bukkit.Main.functions.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandRegister implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("register", new CommandRegister());
    }
    FPI fpi = Functions.instance.getAPI();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (!Accounts.register()) {
            sender.sendMessage(Accounts.noRegister());
            return true;
        }
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (!PermissionsUtils.hasPermissionsSendMessage(p,"functions.default.command.register")) {
                return true;
            }
            Account account = new Account(p.getUniqueId());
            if (args.length < 2) {
                sender.sendMessage(fpi.subcmd());
                return true;
            }
            if (account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsLogin","&c你的账号已登录。退出请使用/logout",p));
                return true;
            }
            if (account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountExists","&c你的账号已注册。",p));
                return true;
            }
            if (!args[0].equals(args[1])) {
                sender.sendMessage(fpi.putLanguage("PasswordEquals","&c密码不一！",p));
                return true;
            }
            if (account.Register(args[0])) {
                sender.sendMessage(fpi.putLanguage("AccountRegister","&a成功注册与登陆！",p));
            } else {
                sender.sendMessage(fpi.putLanguage("AccountNotRegister","&c注册与登陆失败！",p));
            }
            return true;
        }
        sender.sendMessage(fpi.noPlayer());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.default.command.register")) {
            if (args.length <= 2) {
                ls.add("password");
            }
        }
        return ls;
    }
}
