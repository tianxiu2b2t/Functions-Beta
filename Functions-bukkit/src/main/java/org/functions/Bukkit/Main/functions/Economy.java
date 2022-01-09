package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.Main.DataBase;
import org.functions.Bukkit.Main.Functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Economy {
    UUID uuid;
   // DataBase db = Functions.instance.database;
    //String table = Functions.instance.getTable("Economy");
    //public String select_all = "SELECT * FROM " + Functions.instance.getTable("Economy");
    public Economy(UUID uuid) {
        this.uuid = uuid;
        if (!exists()) {
            Functions.instance.yamlUsers().createUser(uuid);
        }
    }
    public double getBalance() {
        return Functions.instance.yamlUsers().configurations.get(uuid).getDouble("Economy",0.0D);
    }
    public void setBalance(double amount) {
        Functions.instance.yamlUsers().set(uuid,"Economy",amount);//db.execute("UPDATE " + table + " SET Economy='" + amount + "' WHERE UUID='" + uuid.toString() +"'");
    }
    public boolean giveBalance(double amount) {
        return addBalance(amount);
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
    public Bank getBank() {
        return new Bank(uuid);
    }
    public boolean putBalance(double amount) {
        if (getBank().getBalance()>=0) {
            if (getBank().getBalance() - amount >= 0) {
                addBalance(amount);
                getBank().takeBalance(amount);
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
                return Utils.parseDoubleUpFromNumber(getBalance(), Functions.instance.getConfiguration().getSettings().getInt("Money.limitation", 4));
            }
            return getBalance();
        }
        return -1;
    }
    public boolean exists() {
        /*List<String> ls = new ArrayList<>();
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
        ls.clear();*/
        return Functions.instance.yamlUsers().exists(uuid);
    }
}
