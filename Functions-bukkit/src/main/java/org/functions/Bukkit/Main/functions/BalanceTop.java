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
        try {
            ResultSet r = Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Economy"));
            while (r.next()) {
                user.add(new Economy(UUID.fromString(r.getString("UUID"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Economy> getEconomyTop() {
        return BalanceTopRunnable.getEconomyTop();
    }
    public List<Bank> getBankBalances() {
        List<Bank> user = new ArrayList<>();
        try {
            ResultSet r = Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Economy"));
            while (r.next()) {
                user.add(new Bank(UUID.fromString(r.getString("UUID"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Bank> getBankTop() {
        return BalanceTopRunnable.getBankTop();
    }
}
