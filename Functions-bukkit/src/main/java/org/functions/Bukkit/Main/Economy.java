package org.functions.Bukkit.Main;

import java.sql.SQLException;
import java.util.UUID;

public class Economy {
    UUID uuid;
    DataBase db = Functions.instance.database;
    String table = Functions.instance.getTable("Economy");
    public Economy(UUID uuid) {
        this.uuid = uuid;
    }
    public double getBalance() {
        try {
            return db.query("SELECT * FROM " + table + " WHERE UUID='" + uuid.toString() + "'").getDouble("Economy");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void setBalance(double amount) {
        db.execute("UPDATE " + table + " SET Economy='" + amount + "' WHERE UUID='" + uuid.toString() +"'");
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
    public boolean hasBalance(double amount) {
        double temp = getBalance() - amount;
        return temp >= 0.0;
    }
    public String display() {
        return getBalance() + Functions.instance.getConfiguration().getSettings().getString("Money.Format","$");
    }
}
