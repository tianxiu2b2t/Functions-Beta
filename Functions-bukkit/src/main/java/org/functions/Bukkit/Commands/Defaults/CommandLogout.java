package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Account;
import org.functions.Bukkit.Main.functions.Accounts;

import java.util.List;

public class CommandLogout implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("logout", new CommandLogout());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            Account account = new Account(p.getUniqueId());
            if (!account.exists()) {
                sender.sendMessage(fpi.putLanguage("AccountNotExists","&c你的账号没有注册。请使用/register <密码> <重复密码> 来注册！",p));
                return true;
            }
            if (!account.isLogin()) {
                sender.sendMessage(fpi.putLanguage("AccountIsNotLogin","&c你的账号没有登录。请使用/login <密码> 登陆！",p));
                return true;
            }
            if (account.logout()) {
                sender.sendMessage(fpi.putLanguage("AccountLogOut", "&a你的账号已退出。",p));
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
