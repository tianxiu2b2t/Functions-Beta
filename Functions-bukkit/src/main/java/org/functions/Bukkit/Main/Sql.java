package org.functions.Bukkit.Main;

import java.sql.*;

public class Sql implements DataBase {
    boolean driver = false;
    String name = "example.db";
    Connection connect = null;
    Statement state = null;
    public Sql(String name) {
        this.name = name;
        //init();
        //connect();
    }
    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            driver = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void connect() {
        try {
            if (driver) {
                connect = DriverManager.getConnection("jdbc:sqlite:" + name);
                state = connect.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean execute(String cmd) {
        try {
            //init();
            //connect();
            connect.setAutoCommit(true);
            state.executeUpdate(cmd);
            //connect.commit();
            //state.close();
            //connect.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //new SQLException(e);
            return false;
        }
    }
    public ResultSet query(String cmd) {
        try {
            //init();
            //connect();
            return state.executeQuery(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean connecting() {
        return driver;
    }
    public Connection getConnection() {
        return connect;
    }
    public boolean changenumber(Integer integer) {
        return integer != 0;
    }

    public void disconnect() {
        try {
            state.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
