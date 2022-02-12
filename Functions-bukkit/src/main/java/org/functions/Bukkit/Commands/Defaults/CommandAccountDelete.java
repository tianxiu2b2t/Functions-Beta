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
import org.functions.Bukkit.Main.functions.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandAccountDelete implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("accountDelete", new CommandAccountDelete());
    }
    FPI fpi = Functions.instance.getAPI();
    Account account;
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                if (!PermissionsUtils.hasPermissionsSendMessage(p, "functions.default.command.accountdelete")) {
                    return true;
                }
                Account account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();

                if (!account.exists()) {
                    sender.sendMessage(fpi.putLanguage("AccountNotExists", "&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！", p));
                    return true;
                }
                if (!account.isLogin()) {
                    sender.sendMessage(fpi.putLanguage("AccountIsNotLogin", "&c你的账号没有登录。请使用/login <密码> 登陆！", p));
                    return true;
                }
                if (account.delete()) {
                    Accounts.accounts.remove(account);
                    sender.sendMessage(fpi.putLanguage("AccountIsDelete", "&a成功删除账号！", p));
                    return true;
                }
            }
        }
        if (sender.isOp()) {
            if (args.length < 1) {
                sender.sendMessage(fpi.subcmd());
                return true;
            }
            String name = args[0];
            Account account;
            if ((account = Accounts.getAccount(name)) != null) {
                account.delete();
                Accounts.reloadAccounts();
                sender.sendMessage(fpi.putLanguage("AccountIsAdministratorDelete", "&a成功删除玩家账号！", null));
                return true;
            }
            sender.sendMessage(fpi.putLanguage("AccountsIsExists","&c账号不存在？",null));
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (sender.isOp()) {
            for (Account u : Accounts.getAccounts()) {
                if (args.length >= 1) {
                    if (u.getName().toLowerCase().contains(args[0])) {
                        ls.add(u.getName().toLowerCase());
                    }
                } else {
                    ls.add(u.getName().toLowerCase());
                }
            }
        }
        Collections.sort(ls);
        return ls;
    }
}
