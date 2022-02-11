package org.functions.Bukkit.Main;

import java.io.File;
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
            if (state.isClosed()) {
                init();
                connect();
            }
            connect.setAutoCommit(false);
            state.executeUpdate(cmd);
            connect.commit();
            //connect.commit();
            //state.close();
            //connect.close();
            disconnect();
            return true;
        } catch (SQLException e) {
            e.fillInStackTrace();
            //File file = new File(name);
            //file.renameTo(new File(file.getName() + "_Back" + Functions.instance.getAPI().getDate() + Functions.instance.getAPI().getDateTime()));
            //new SQLException(e);
            return false;
        }
    }
    public ResultSet query(String cmd) {
        try {
            //init();
            //connect();
            if (state.isClosed()) {
                init();
                connect();
            }
            ResultSet resultSet = state.executeQuery(cmd);
            disconnect();
            return resultSet;
        } catch (SQLException e) {
            e.fillInStackTrace();
            //File file = new File(name);
            //file.renameTo(new File(file.getName() + "_Back" + Functions.instance.getAPI().getDate() + Functions.instance.getAPI().getDateTime()));
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
            connect.commit();
            if (!state.isClosed()) {
                state.close();
            }
            if (!connect.isClosed()) {
                connect.close();
            }
            //Thread.sleep(5L);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void deleteFile() {
        File file = new File(name);
        Functions.instance.print(name);
        if (file.exists()) {
            disconnect();
            file.deleteOnExit();
            connect();
        }
    }

    @Override
    public boolean parseBoolean(int i) {
        return i != 0;
    }
}
