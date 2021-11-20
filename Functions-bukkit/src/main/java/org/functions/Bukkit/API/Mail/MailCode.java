package org.functions.Bukkit.API.Mail;

import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.functions.Account;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.RandomCode;

import java.util.UUID;

public class MailCode {
    UUID uuid;
    FPI fpi;
    public MailCode(UUID uuid) {
        this.uuid = uuid;
    }
    public boolean verify(String code) {
        if (FPI.code.get(uuid)!=null) {
            if (FPI.code.get(uuid).equalsIgnoreCase(code)) {
                FPI.code.remove(uuid);
                FPI.code_verify.put(uuid,true);
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean create() {
        int i = 0;
        Account account = new Account(uuid);
        if (Functions.instance.getConfiguration().getSettings().getString("Mail.From")!=null) {
            if (account.existsMail()) {
                RandomCode random = new RandomCode(RandomCode.Type.ALL, 6);
                if (FPI.code.get(uuid) == null) {
                    String code = random.out();
                    if (Mail.sendRecoveryPassword(account.getMail(),
                            account.getPlayer(),
                            code,
                            Functions.instance.getConfiguration().getSettings().getString("Mail.TimeOut", "5"))) {
                        FPI.code.put(uuid, code);
                        FPI.code_timeout.put(uuid, (System.currentTimeMillis() + 1000 * 60 * Functions.instance.getConfiguration().getSettings().getLong("Mail.TimeOut", 5)));
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
