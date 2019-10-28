/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zxc
 */
public class Database {
//    Init database variable
    String DB_USERNAME = "admin";
    String DB_PASSWORD = "987";
    String DB_DBNAME = "chatdb";
    String DB_URL = String.format("jdbc:mysql://localhost:3306/%s", DB_DBNAME);
    String DB_DRV = "com.mysql.jdbc.Driver";
    
//    Init singleton variable
    private static Database instance = null;
    private static  Connection conn = null;
    
    private Database() {
        try {
            Class.forName(DB_DRV);
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connnected!");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection getConnection() {
        if(instance == null) {
            instance = new Database();
        }
    	
    	return instance.conn;
    }
    
    public static void close() {
        try {
            conn.close();
            System.out.println("Database connection closed!");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
