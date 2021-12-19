package org.functions.Bukkit.Main;

import java.sql.Connection;
import java.sql.ResultSet;

public interface DataBase {
    public void connect();
    public void init();
    public boolean execute(String cmd);
    public ResultSet query(String cmd);
    public boolean connecting();
    public Connection getConnection();
    public boolean changenumber(Integer integer);
    public void disconnect();
    public void deleteFile();
    public boolean parseBoolean(int i);
}
