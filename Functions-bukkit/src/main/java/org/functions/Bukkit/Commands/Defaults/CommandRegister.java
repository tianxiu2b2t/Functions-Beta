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

public class CommandRegister implements TabExecutor {
    public void run() {
        new FPI().getCommand("register", new CommandRegister());
    }
    FPI fpi = new FPI();
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
            Account account = new Account(p.getUniqueId());
            if (args.length < 2) {
                sender.sendMessage(fpi.subcmd());
                return true;
            }
            if (account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsLogin","&c你的账号已登录。退出请使用/logout"));
                return true;
            }
            if (account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountExists","&c你的账号已注册。"));
                return true;
            }
            if (!args[0].equals(args[1])) {
                sender.sendMessage(fpi.putLanguage("PasswordEquals","&c密码不一！"));
                return true;
            }
            if (account.Register(args[0])) {
                sender.sendMessage(fpi.putLanguage("AccountRegister","&a成功注册与登陆！"));
            } else {
                sender.sendMessage(fpi.putLanguage("AccountNotRegister","&c注册与登陆失败！"));
            }
            return true;
        }
        sender.sendMessage(fpi.noPlayer());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
