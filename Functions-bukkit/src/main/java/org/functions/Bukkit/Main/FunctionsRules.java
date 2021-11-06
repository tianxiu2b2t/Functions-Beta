package org.functions.Bukkit.Main;

import org.functions.Bukkit.API.FPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FunctionsRules {
    DataBase db = Functions.instance.getDatabase();
    String table = Functions.instance.getTable("Rules");
    public enum Type {
        FALL,
        SICKS_ITEM,
        DISPENSER,
    }
    public FunctionsRules() {
        AddRules(Type.FALL);
        AddRules(Type.SICKS_ITEM);
        AddRules(Type.DISPENSER);
    }
    public FunctionsRules clone() {
        try {
            return (FunctionsRules) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
    public boolean isEnabled(Type type) {
        try {
            return  Boolean.parseBoolean(db.query("SELECT * FROM " + table + " WHERE Rules='" + type.name() + "'").getString("Enable"));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean enable(Type type) {
        if (isEnabled(type)) {
            db.execute("UPDATE " + table + " SET Enable='" + false + "' WHERE Rules=' " + type.name() + "')");
            return false;
        }
        db.execute("UPDATE " + table + " SET Enable='" + true + "' WHERE Rules=' " + type.name() + "')");
        return true;
    }
    private boolean RulesExists(Type type) {
        List<String> ls = new ArrayList<>();
        ResultSet rs = db.query("SELECT * FROM " + table);
        try {
            while (rs.next()) {
                ls.add(rs.getString("Rules"));
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }
        for (String s : ls) {
            if (s.equalsIgnoreCase(type.name().toLowerCase())) {
                ls.clear();
                return true;
            }
        }
        ls.clear();
        return false;
    }
    private void AddRules(Type type) {
        if (!RulesExists(type)) {
            db.execute("INSERT INTO " + table + " ( Rules  ) VALUES ( '" + type.name() + "' )");
        }
    }
}
