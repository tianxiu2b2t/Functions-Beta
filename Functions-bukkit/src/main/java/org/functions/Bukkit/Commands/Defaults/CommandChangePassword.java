package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.functions.Account;
import org.functions.Bukkit.Main.functions.Accounts;

import java.util.List;

public class CommandChangePassword implements TabExecutor {
    public void run() {
        new FPI().getCommand("changepassword", new CommandChangePassword());
    }
    FPI fpi = new FPI();
    Account account;
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            account = new Account(p.getUniqueId());
            if (!account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountNotExists","&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！",p));
                return true;
            }
            if (!account.isLogin()) {
                if (args.length < 2) {
                    sender.sendMessage(fpi.subcmd());
                    return true;
                }
                if (FPI.code_verify.get(p.getUniqueId())!=null) {
                    if (!account.changePassword(args[0])) {
                        sender.sendMessage(fpi.putLanguage("PasswordEquals","&c原密码不能与新密码一样！",p));
                        return true;
                    }
                    if (!args[0].equals(args[1])) {
                        sender.sendMessage(fpi.putLanguage("PasswordNotEquals","&c密码不一！",p));
                        return true;
                    }
                    sender.sendMessage(fpi.putLanguage("AccountChangePassword", "&a密码成功换成： %password%！",p).replace("%password%", args[1]));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("NoLoginChangePassword","&c你没有获取验证码！",p));
            } else {
                if (args.length < 3) {
                    sender.sendMessage(fpi.subcmd());
                    return true;
                }
                if (!account.changePassword(args[1])) {
                    sender.sendMessage(fpi.putLanguage("PasswordEquals","&c原密码不能与新密码一样！",p));
                    return true;
                }
                if (!args[1].equals(args[2])) {
                    sender.sendMessage(fpi.putLanguage("PasswordNotEquals","&c密码不一！",p));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("AccountChangePassword", "&a密码成功换成： %password%！",p).replace("%password%", args[1]));
                return true;
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
