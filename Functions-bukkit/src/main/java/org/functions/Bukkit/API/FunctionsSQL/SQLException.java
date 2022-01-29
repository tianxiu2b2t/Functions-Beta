package org.functions.Bukkit.API.FunctionsSQL;

public class SQLException extends RuntimeException {
    public SQLException(String s, String args) {
        if (args.equalsIgnoreCase("select")) {
            if (s != null) {
                throw new SQLException(s);
            } else {
                throw new SQLException("SQL Exception: No select");
            }
        } else if (args.equalsIgnoreCase("outIndex")) {
            if (s != null) {
                throw new SQLException("SQL Exception:" + s);
            } else {
                throw new SQLException("SQL Exception: Out Index.");
            }
        }
    }
    public SQLException(String s) {
        super(s);
    }
}
