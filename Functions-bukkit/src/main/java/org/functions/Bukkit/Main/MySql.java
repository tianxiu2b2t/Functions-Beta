package org.functions.Bukkit.Main;

import java.sql.*;

public class MySql implements DataBase {
    boolean driver = false;
    String name = "example";
    Connection connect = null;
    Statement state = null;
    String user = "root";
    String password = "";
    public MySql(String name,String user,String password) {
        this.name = name;
        this.user = user;
        this.password = password;
        init();
    }
    public void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            driver = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void connect() {
        try {
            if (driver) {
                connect = DriverManager.getConnection("jdbc:mysql://lt.limc.cc:38302/" + name, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean execute(String cmd) {
        try {
            connect();
            connect.setAutoCommit(true);
            state = connect.createStatement();
            state.executeUpdate(cmd);
            state.close();
            connect.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ResultSet query(String cmd) {
        try {
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

    @Override
    public boolean changenumber(Integer integer) {
        return false;
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
