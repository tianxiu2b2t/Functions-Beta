package org.functions.Bukkit.Tasks;

import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Account;
import org.functions.Bukkit.Main.Accounts;

public class CheckAccountLogin implements Runnable {
    FPI fpi = new FPI();
    public void run() throws NullPointerException {
        if (Accounts.enable()) {
            for (Player p : fpi.getOnlinePlayers()) {
                if (!(new Account(p.getUniqueId()).exists())) {
                    p.sendMessage(fpi.putLanguage("RegisterAccount", "&c请使用/register <密码> <重复密码> 来注册账号！"));
                }
                if (Accounts.getAccount(p.getUniqueId()).exists()) {
                    if (!Accounts.getAccount(p.getUniqueId()).isLogin()) {
                        Accounts.login.put(p.getUniqueId(), false);
                    }
                }
                if (!Accounts.getAccount(p.getUniqueId()).isLogin()) {
                    Accounts.login.put(p.getUniqueId(), false);
                }
                if (!Accounts.login.get(p.getUniqueId())) {
                    for (Account account : Accounts.getAccounts()) {
                        if (account.getUniqueID().equals(p.getUniqueId())) {
                            if (account.isLogin()) {
                                continue;
                            }
                            p.sendMessage(fpi.putLanguage("LoginAccount", "&c请使用/login <密码> 来登录"));
                        }
                    }
                }
            }
        }
    }
}
