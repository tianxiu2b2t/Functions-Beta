package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.UserAccounts.Account;
import org.functions.Bukkit.Main.functions.UserAccounts.Accounts;
import org.functions.Bukkit.Main.functions.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandLogin implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("login", new CommandLogin());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (!PermissionsUtils.hasPermissionsSendMessage(p,"functions.default.command.login")) {
                return true;
            }
            Account account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();

            if (args.length < 1) {
                sender.sendMessage(fpi.subcmd());
                return true;
            }
            if (!account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountNotExists","&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！",p));
                return true;
            }
            if (account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsLogin","&c你的账号已登录。退出请使用/logout",p));
                return true;
            }
            if (account.login(args[0])) {
                sender.sendMessage(fpi.putLanguage("AccountLogin","&a成功登陆！",p));
            } else {
                sender.sendMessage(fpi.putLanguage("AccountPasswordWrong","&c密码错误！",p));
            }
            return true;
        } else {
            sender.sendMessage(fpi.noPlayer());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.default.command.login")) {
            if (args.length <= 1) {
                ls.add("password");
            }
        }
        return ls;
    }
}
