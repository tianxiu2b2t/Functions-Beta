package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Account;
import org.functions.Bukkit.Main.Accounts;

import java.util.List;

public class CommandLogin implements TabExecutor {
    public void run() {
        new FPI().getCommand("login", new CommandLogin());
    }
    FPI fpi = new FPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            Account account = new Account(p.getUniqueId());
            if (args.length < 1) {
                sender.sendMessage(fpi.subcmd());
                return true;
            }
            if (!account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountNotExists","&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！"));
                return true;
            }
            if (account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsLogin","&c你的账号已登录。退出请使用/logout"));
                return true;
            }
            if (account.Login(args[0])) {
                sender.sendMessage(fpi.putLanguage("AccountLogin","&a成功登陆！"));
            } else {
                sender.sendMessage(fpi.putLanguage("AccountPasswordWrong","&c密码错误！"));
            }
            return true;
        } else {
            sender.sendMessage(fpi.noPlayer());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
