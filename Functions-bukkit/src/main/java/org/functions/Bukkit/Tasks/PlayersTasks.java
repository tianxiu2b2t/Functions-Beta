package org.functions.Bukkit.Tasks;

import org.bukkit.entity.Player;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Accounts;
import org.functions.Bukkit.Main.Functions;

public class PlayersTasks implements Runnable {
    FPI fpi = new FPI();
    public void run() {
        for (Player p : fpi.getOnlinePlayers()) {
            if (FPI.code_timeout.get(p.getUniqueId()) != null) {
                if (FPI.code_timeout.get(p.getUniqueId()) < System.currentTimeMillis()) {
                    FPI.code_timeout.remove(p.getUniqueId());
                    FPI.code.remove(p.getUniqueId());
                    FPI.code_verify.remove(p.getUniqueId());
                }
            }
        }
    }
}
