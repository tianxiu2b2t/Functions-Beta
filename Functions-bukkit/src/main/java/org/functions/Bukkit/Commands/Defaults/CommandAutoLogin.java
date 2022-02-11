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

import java.util.List;

public class CommandAutoLogin implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("autologin", new CommandAutoLogin());
    }
    FPI fpi = Functions.instance.getAPI();
    Account account;
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (!PermissionsUtils.hasPermissionsSendMessage(p,"functions.default.command.autologin")) {
                return true;
            }
            Account account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();

            if (!account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountNotExists","&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！",p));
                return true;
            }
            if (!account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsNotLogin","&c你的账号没有登录。请使用/login <密码> 登陆！",p));
                return true;
            }
            if (account.setAutoLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountEnableAutoLogin","&a自动登陆登陆为： %enable%",p).replace("%enable%",fpi.changeBooleanToText(account.getAutoLogin())));
                return true;
            }
            sender.sendMessage(fpi.putLanguage("AccountNoEnableAutoLogin","&c开启或关闭失败！",p));
            return true;
        }
        sender.sendMessage(fpi.noPlayer());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
