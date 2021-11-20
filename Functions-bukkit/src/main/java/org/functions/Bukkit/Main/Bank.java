package org.functions.Bukkit.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bank {
    UUID uuid;
    DataBase db = Functions.instance.database;
    String table = Functions.instance.getTable("Economy");
    public String select_all = "SELECT * FROM " + Functions.instance.getTable("Economy");
    public Bank(UUID uuid) {
        this.uuid = uuid;
        if (!exists()) {
            db.execute("INSERT INTO " + table + " ( UUID , Economy , Bank ) VALUES ( '" + uuid.toString() + "' , ' 0 ', ' 0 ' )");
        }
    }
    public boolean exists() {
        List<String> ls = new ArrayList<>();
        ResultSet rs = db.query(select_all);
        try {
            while (rs.next()) {
                ls.add(rs.getString("UUID"));
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }
        for (String s : ls) {
            if (s.equalsIgnoreCase(uuid.toString())) {
                ls.clear();
                return true;
            }
        }
        ls.clear();
        return false;
    }
    public double getBalance() {
        try {
            return db.query("SELECT * FROM " + table + " WHERE UUID='" + uuid.toString() + "'").getDouble("Bank");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    public void setBalance(double amount) {
        db.execute("UPDATE " + table + " SET Bank='" + amount + "' WHERE UUID='" + uuid.toString() +"'");
    }
    public boolean giveBalance(double amount) {
        return addBalance(amount);
    }
    public Economy getEconomy() {
        return new Economy(uuid);
    }
    public boolean putBalance(double amount) {
        if (getEconomy().getBalance()>=0) {
            if ((getEconomy().getBalance() - amount) >= 0) {
                addBalance(amount);
                getEconomy().takeBalance(amount);
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean addBalance(double amount) {
        double temp = amount + getBalance();
        if (amount>=0.0) {
            if (temp>=0.0) {
                setBalance(temp);
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean takeBalance(double amount) {
        double temp = getBalance() - amount;
        if (amount>=0.0) {
            if (temp>=0.0) {
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
        return autoGetBalance() + Functions.instance.getConfiguration().getSettings().getString("Money.Format","$");
    }
    public double autoGetBalance() {
        if (getBalance()>=0.0) {
            if ((getBalance() + "").split("\\.")[1].length() >= (Functions.instance.getConfiguration().getSettings().getInt("Money.limitation", 4) + 1)) {
                return Util.parseDoubleUpFromNumber(getBalance(), Functions.instance.getConfiguration().getSettings().getInt("Money.limitation", 4));
            }
            return getBalance();
        }
        return -1;
    }
}
