/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Date;

/**
 *
 * @author zxc
 */
public class Message {
    String username;
    String message;
    Date date_at;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
        this.date_at = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate_at() {
        return date_at;
    }

    public void setDate_at(Date date_at) {
        this.date_at = date_at;
    }
}
