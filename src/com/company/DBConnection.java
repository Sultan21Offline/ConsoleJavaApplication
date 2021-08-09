package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String url = "jdbc:postgresql://localhost:5432/Shop?createDatabaseIfNotExist=true";
    private String user = "postgres";
    private String password = "Qweasdzxc1Q";
    private Connection connection;

    public DBConnection() throws SQLException{
        connection = DriverManager.getConnection(url,user,password);
    }
    public Connection getConnection(){
        return connection;
    }
}
