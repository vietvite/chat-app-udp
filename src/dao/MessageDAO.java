/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bean.Account;
import bean.Message;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author zxc
 */
public class MessageDAO {
    
    public int saveMessage(Message message) throws SQLException, ClassNotFoundException {
        String query = String.format(
                "INSERT INTO `message`(`message`, `date_at`, `username`) VALUES ('%s','%s','%s')",
                message.getMessage(),
                message.getDate_at(),
                message.getUsername());
        Statement cmd = Database.getConnection().createStatement();
        int isSuccess = cmd.executeUpdate(query);

        return isSuccess;
    }
}
