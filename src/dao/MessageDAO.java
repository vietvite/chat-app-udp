/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bean.Account;
import bean.Message;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zxc
 */
public class MessageDAO {
    
    public int saveMessage(Message message) throws SQLException, ClassNotFoundException {
        String query = String.format(
                "INSERT INTO `message`(`message`, `date_at`, `username`) VALUES ('%s','%s','%s')",
                message.getMessage(),
                new java.sql.Date(message.getDate_at()),
                message.getUsername());
        Statement cmd = Database.getConnection().createStatement();
        int isSuccess = cmd.executeUpdate(query);

        return isSuccess;
    }
    
    public ResultSet getAllMessages(String username) {
        try {
            String query = "SELECT * FROM message WHERE username='"+username+"'";
            Statement cmd = Database.getConnection().createStatement();
            ResultSet rs = cmd.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ResultSet findMessagesByDate(String date, String username) {
        try {
            String query = "SELECT * FROM message WHERE date_at='"+date+"' AND username='" + username + "'";
            Statement cmd = Database.getConnection().createStatement();
            ResultSet rs = cmd.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public DefaultTableModel getMessageTable(String username) throws SQLException {
        DefaultTableModel mh = new DefaultTableModel();
        
        ResultSet rs = getAllMessages(username);
        
//        get col header of table
        ResultSetMetaData mt = rs.getMetaData();
        
//        lay ten cot trong rs -> gan vao defaultTableModel
        int sc = mt.getColumnCount();
        for(int i = 1; i <= sc; i++) {
            mh.addColumn(mt.getColumnName(i));
        }
        
        while(rs.next()) {
            Object[] t = new Object[sc];
            for(int j = 1; j <= sc; j++) {
                t[j-1] = rs.getString(j);
            }
            mh.addRow(t);
        }
        
        return mh;
    }
    
    public DefaultTableModel findMessageTableByDate(String date, String username) throws SQLException {
        DefaultTableModel mh = new DefaultTableModel();
        
        ResultSet rs = findMessagesByDate(date, username);
        
//        get col header of table
        ResultSetMetaData mt = rs.getMetaData();
        
//        lay ten cot trong rs -> gan vao defaultTableModel
        int sc = mt.getColumnCount();
        for(int i = 1; i <= sc; i++) {
            mh.addColumn(mt.getColumnName(i));
        }
        
        while(rs.next()) {
            Object[] t = new Object[sc];
            for(int j = 1; j <= sc; j++) {
                t[j-1] = rs.getString(j);
            }
            mh.addRow(t);
        }
        
        return mh;
    }
}
