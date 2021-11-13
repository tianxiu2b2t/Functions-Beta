package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Account;

import java.util.List;

public class CommandAutoLogin implements TabExecutor {
    public void run() {
        new FPI().getCommand("autologin", new CommandAutoLogin());
    }
    FPI fpi = new FPI();
    Account account;
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            Account account = new Account(p.getUniqueId());
            if (!account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountNotExists","&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！"));
                return true;
            }
            if (!account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsNotLogin","&c你的账号没有登录。请使用/login <密码> 登陆！"));
                return true;
            }
            if (account.setAutoLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountEnableAutoLogin","&a自动登陆登陆为： %enable%").replace("%enable%",fpi.changeBooleanToText(account.getAutoLogin())));
                return true;
            }
            sender.sendMessage(fpi.putLanguage("AccountNoEnableAutoLogin","&c开启或关闭失败！"));
            return true;
        }
        sender.sendMessage(fpi.noPlayer());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
