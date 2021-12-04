package org.functions.Bukkit.Tasks;

import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.*;

public class Tasks implements Runnable {
    FPI fpi = Functions.instance.getAPI();
    long check = 0;
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
        fpi.getInstance().up.run();
    }
}
