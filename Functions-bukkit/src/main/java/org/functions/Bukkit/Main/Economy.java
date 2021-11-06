package org.functions.Bukkit.Main;

import java.sql.SQLException;
import java.util.UUID;

public class Economy {
    UUID uuid;
    public Economy(UUID uuid) {
        this.uuid = uuid;
    }
    public double getBalance() {
        try {
            return Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Economy") + " WHERE UUID='" + uuid.toString() + "'").getDouble("Economy");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void setBalance(double amount) {
        Functions.instance.getDatabase().execute("UPDATE " + Functions.instance.getTable("Economy") + " SET Economy='" + amount + "' WHERE UUID='" + uuid.toString() +"'");
    }
    public boolean addBalance(double amount) {
        double temp = amount + getBalance();
        if (amount>0.0) {
            if (temp>0.0) {
                setBalance(temp);
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean takeBalance(double amount) {
        double temp = getBalance() - amount;
        if (amount>0.0) {
            if (temp>0.0) {
                setBalance(temp);
                return true;
            }
            return false;
        }
        return false;
    }
}
