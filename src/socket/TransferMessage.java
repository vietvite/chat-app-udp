/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zxc
 */
public class TransferMessage {
    DatagramSocket serverSocket;
    
    public void openPort(int port) {
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("Message port " + port + " opened");
        } catch (SocketException ex) {
            Logger.getLogger(TransferMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String receiveMessage() {
        try {
            byte[] buff = new byte[512];
            DatagramPacket pkg = new DatagramPacket(buff, buff.length);
            serverSocket.receive(pkg);
            String message = new String(pkg.getData(), 0, pkg.getLength());
            return message;
        } catch (IOException ex) {
            Logger.getLogger(TransferMessage.class.getName()).log(Level.SEVERE, null, ex);
            return "Missing message";
        }
    }
    
    public void sendMessage(String ip, int port, String message) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress ipAddr = InetAddress.getByName(ip);
            System.out.println("ipAddr: " + ipAddr);
            
            DatagramPacket pkg = new DatagramPacket(message.getBytes(), message.length(), ipAddr, port);
            clientSocket.send(pkg);
            clientSocket.close();
            System.out.println("`" + message + "` sent to IP: " + ip + ", port: " + port);
            
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(TransferMessage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TransferMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
