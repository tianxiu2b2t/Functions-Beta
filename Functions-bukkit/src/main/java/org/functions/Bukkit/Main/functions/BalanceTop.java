package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Tasks.BalanceTopRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BalanceTop {
    public List<Economy> getEconomyBalances() {
        List<Economy> user = new ArrayList<>();
        Functions.instance.getPlayerManager().getAllUser().forEach(e->{
            user.add(e.getEconomy());
        });
        return user;
    }

    public List<Economy> getEconomyTop() {
        return BalanceTopRunnable.getEconomyTop();
    }
    public List<Bank> getBankBalances() {
        List<Bank> user = new ArrayList<>();
        Functions.instance.getPlayerManager().getAllUser().forEach(e->{
            user.add(e.getBank());
        });
        return user;
    }

    public List<Bank> getBankTop() {
        return BalanceTopRunnable.getBankTop();
    }
}
