package org.functions.Bukkit.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Account;
import org.functions.Bukkit.Main.functions.Accounts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class CheckAccountLogin implements Runnable {
    List<UUID> NoLogins = new ArrayList<>();
    FPI fpi = Functions.instance.getAPI();
    public void run() {
        NoLogins.clear();
        Accounts.login.forEach((e,f)->{
            if (!f) {
                NoLogins.add(e);
            }
        });
        NoLogins.forEach(e->{
            if (!Bukkit.getPlayer(e).isOnline()) NoLogins.remove(e);
            Account account = Functions.instance.getPlayerManager().getUser(e).getAccount();
            boolean is = false;
            System.out.println("a");
            if (account.exists()) {
                System.out.println("b");
                account.getPlayer().sendMessage(fpi.putLanguage("LoginAccount", "&c请使用/login <密码> 或者使用/mailogin 来登录", account.getPlayer()));
                is = true;
                System.out.println("c");
            }
            System.out.println("e");
            if (!is) {
                System.out.println("d");
                account.getPlayer().sendMessage(fpi.putLanguage("RegisterAccount", "&c请使用/register <密码> <重复密码> 来注册账号！", account.getPlayer()));
            }
        });
    }
}
