package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.Main.Functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Operators {
    UUID uuid;
    public Operators(UUID uuid) {
        this.uuid = uuid;
        if (!exists()) {
            Functions.instance.getDatabase().execute("INSERT INTO " + Functions.instance.getTable("Operators") + " ( UUID, Operator ) VALUES ( ‘" + uuid.toString() + "’ , ' false ' )");
        }
    }
    public boolean query() {
        try {
            return Boolean.parseBoolean(Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Operators") + " WHERE UUID='" + uuid.toString() + "'").getString("Operator"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void set() {
        if (query()) Functions.instance.getDatabase().execute("UPDATE " + Functions.instance.getTable("Operators") + " SET Operator='." + false + "' WHERE UUID='" + uuid.toString() + "'");
        if (!query()) Functions.instance.getDatabase().execute("UPDATE " + Functions.instance.getTable("Operators") + " SET Operator='" + true + "' WHERE UUID='" + uuid.toString() + "'");
    }
    public void check() {
        Functions.instance.getAPI().getServer().getPlayer(uuid).setOp(query());
    }
    public boolean exists() {
        List<String> ls = new ArrayList<>();
        ResultSet rs = Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Operators"));
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
}
