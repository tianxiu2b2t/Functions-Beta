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

public class CommandChangePassword implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("changepassword", new CommandChangePassword());
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
            if (!PermissionsUtils.hasPermissionsSendMessage(p,"functions.default.command.changepassword")) {
                return true;
            }
            Account account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();

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
        List<String> ls = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return ls;
        }
        Player p = ((Player) sender).getPlayer();
        Account account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();
        if (!PermissionsUtils.hasPermissionsSendMessage(p,"functions.default.command.changepassword")) {
            if (account.isLogin()) {
                if (args.length <= 1) ls.add("旧密码");
                if (args.length == 2 || args.length == 3) ls.add("新密码");
            } else {
                if (args.length <= 2) ls.add("新密码");
            }
        }
        return ls;
    }
}
