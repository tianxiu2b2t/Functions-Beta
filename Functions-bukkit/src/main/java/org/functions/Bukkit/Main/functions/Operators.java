package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.Main.Functions;

import java.sql.SQLException;
import java.util.UUID;

public class Operators {
    UUID uuid;
    public Operators(UUID uuid) {
        this.uuid = uuid;
    }
    public boolean query() {
        try {
            return Functions.instance.getDatabase().query("SELECT * FROM " + Functions.instance.getTable("Operators") + " WHERE UUID='" + uuid.toString() + "'").getBoolean("Operator");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void set() {
        Functions.instance.getDatabase().execute("UPDATE " + Functions.instance.getTable("Operators") + " Operator=''");
    }
}
