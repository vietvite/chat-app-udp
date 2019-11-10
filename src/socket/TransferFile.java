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
    class ReceiveFileThread extends Thread {

        @Override
        public void run() {
            while(true) {
                if(isReceivingFile) {
                    try {
                        byte[] buff = new byte[BUFF_SIZE];
                        buff = receiveByte();
                        String data = new String(buff, 0, receiveByteLength);

                        if(data.trim().equals("ENDFILE")) {
                            fileOutput.close();
                            isReceivingFile = false;
                            return;
                        } else {
                            fileOutput.write(buff, 0, receiveByteLength);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    public boolean isReceivingFile = false;
    String ip;
    int toPort;
    int hostPort;
    
    int BUFF_SIZE = 512;
    int receiveByteLength;
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
        } catch (SocketException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendByte(byte[] buff, int buffLength) throws SocketException, UnknownHostException, IOException {
        DatagramSocket client = new DatagramSocket();
        InetAddress IP = InetAddress.getByName(this.ip);
        DatagramPacket pkg = new DatagramPacket(buff, buffLength, IP, this.toPort);
        client.send(pkg);
        client.close();
        System.out.println("Sent buff: " + buff);
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
            System.out.println("File is sending to " + this.toPort + " port.");
            Thread.sleep(5);
            
            int buffFileSize = fileInput.read(buff);
            while(buffFileSize > 0) {
                sendByte(buff, buffFileSize);
                Thread.sleep(2);
                buffFileSize = fileInput.read(buff, 0, buff.length);
            }
            messaging.sendMessage(this.ip, this.toPort, "ENDFILE");
            fileInput.close();
            
        } catch (FileNotFoundException | InterruptedException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void receiveFile(String fileName) {
        try {
            fileOutput = new FileOutputStream("./" + fileName);
            ReceiveFileThread revFileThread = new ReceiveFileThread();
            revFileThread.start();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
