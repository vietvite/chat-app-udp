/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bean.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zxc
 */
public class AccountDAO {
    public int setOnlineState(String username, String isConnect) {
        try {
            String query = String.format("UPDATE `account` SET `isConnect`='%s' WHERE `username`='%s'", isConnect, username);
            Statement cmd = Database.getConnection().createStatement();
            
            return cmd.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int setIp(String username, String ip) {
        try {
            String query = String.format("UPDATE `account` SET `ip`='%s' WHERE `username`='%s'", ip, username);
            Statement cmd = Database.getConnection().createStatement();
            
            return cmd.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        
    }
    
    public boolean checkAccount(String username, String password) {
        try {
            String query = String.format("SELECT * FROM `account` WHERE username='%s' and password='%s'", username, password);
            Statement cmd = Database.getConnection().createStatement();
            ResultSet rs = cmd.executeQuery(query);
            
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public int createAccount(Account user) throws SQLException, ClassNotFoundException {
        String query = String.format(
                "INSERT INTO `account`(`username`, `password`, `fullname`, `ip`, `isConnect`) VALUES ('%s','%s','%s','%s','%s')", 
                user.getUsername() , 
                user.getPassword(), 
                user.getFullname(),
                user.getIp(), 
                user.getIsConnect());
        Statement cmd = Database.getConnection().createStatement();
        int isSuccess = cmd.executeUpdate(query);

        return isSuccess;
    }
    
    public String getIp(String username) {
        try {
            String query = String.format("SELECT ip FROM `account` WHERE username='%s'", username);
            Statement cmd = Database.getConnection().createStatement();
            ResultSet rs = cmd.executeQuery(query);
            
            if(rs.next()) {
                return rs.getString("ip");
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ResultSet getOnline() {
        try {
            String query = "SELECT * FROM `account` WHERE isConnect='1'";
            Statement cmd = Database.getConnection().createStatement();
            
            return cmd.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean getConnectState(String username) {
        try {
            String query = String.format("SELECT isConnect FROM `account` WHERE username='%s'", username);
            Statement cmd = Database.getConnection().createStatement();
            ResultSet rs = cmd.executeQuery(query);
            
            if(rs.next()) {
                return rs.getBoolean("isConnect");
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public String getFullname(String username) {
        try {
            String query = String.format("SELECT fullname FROM `account` WHERE username='%s'", username);
            Statement cmd = Database.getConnection().createStatement();
            ResultSet rs = cmd.executeQuery(query);
            
            if(rs.next()) {
                return rs.getString("fullname");
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public int changePassword(String username, String newPasswd) {
        try {
            String query = String.format("UPDATE `account` SET password='%s' WHERE username='%s'", newPasswd, username);
            Statement cmd = Database.getConnection().createStatement();
            
            return cmd.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
