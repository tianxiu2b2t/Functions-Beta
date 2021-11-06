package org.functions.Bukkit.Main;

import org.functions.Bukkit.Tasks.BalanceTopRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BalanceTop {
    int top = 10;
    public BalanceTop(int top) {
        this.top = top;
    }
    public List<Economy> getBalances() {
        List<Economy> user = new ArrayList<>();
        try {
            ResultSet r = Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Economy"));
            while (r.next()) {
                user.add(new Economy(UUID.fromString(r.getString("UUID"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Economy> getTop() {
        return BalanceTopRunnable.getBalanceTop();
    }
}
