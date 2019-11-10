/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public class TransferFile {
    public boolean isReceivingFile = false;
    public int receiveByteLength;
    
    String ip;
    int toPort;
    int hostPort;
    
    int BUFF_SIZE = 512;
    
    DatagramSocket serverSocket;
    
    FileInputStream fileInput;
    FileOutputStream fileOutput;
    
//    constructor
    public TransferFile() {
    }

    public TransferFile(String ip, int toPort, int hostPort) {
        this.ip = ip;
        this.toPort = toPort;
        this.hostPort = hostPort;
    }
    
    
    public void openPort(int port) {
        this.hostPort = port;
        
        try {
            serverSocket = new DatagramSocket(this.hostPort);
            System.out.println("File port " + this.hostPort + "opened");
        } catch (SocketException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendByte(byte[] buff, int buffLength) throws SocketException, UnknownHostException, IOException {
        
        DatagramSocket client = new DatagramSocket();
        InetAddress IP = InetAddress.getByName(this.ip);
        
//        Destinate port: `this.toPort+1` because current (receiver) port being used by message transfer
        DatagramPacket pkg = new DatagramPacket(buff, buffLength, IP, this.toPort + 1);
        client.send(pkg);
        client.close();
    }
    
    public byte[] receiveByte() {
        try {
            byte[] buff = new byte[BUFF_SIZE];
            DatagramPacket pkg = new DatagramPacket(buff, buff.length);
            serverSocket.receive(pkg);
            byte[] receiveByte = new byte[pkg.getLength()];
            this.receiveByteLength = pkg.getLength();
            receiveByte = pkg.getData();
            
            return receiveByte;
        } catch (IOException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void sendFile(String filePath, String fileName) {
        try {
            
            fileInput = new FileInputStream(filePath);
            String mess = "FILE:" + fileName;
            byte[] buff = new byte[BUFF_SIZE];
            
            TransferMessage messaging = new TransferMessage();
            messaging.sendMessage(this.ip, toPort, mess);
            System.out.println("Start sending file to " + (this.toPort + 1) + " port.");
            Thread.sleep(5);
            
            int buffFileSize = fileInput.read(buff);
            while(buffFileSize > 0) {
                sendByte(buff, buffFileSize);
                Thread.sleep(2);
                buffFileSize = fileInput.read(buff, 0, buff.length);
            }
            
//            Destinate port: `this.toPort+1` because current (receiver) port being used by message transfer
            messaging.sendMessage(this.ip, this.toPort + 1, "ENDFILE");
            fileInput.close();
            
        } catch (FileNotFoundException | InterruptedException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
