/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import bean.Message;
import dao.AccountDAO;
import dao.MessageDAO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import socket.TransferFile;
import socket.TransferMessage;

/**
 *
 * @author zxc
 */
public class frmChat extends javax.swing.JFrame {

    String username;
    String fullname;
    TransferMessage messageSocket = new TransferMessage();
    TransferFile fileSocket = new TransferFile();
    
    AccountDAO handle = new AccountDAO();
    MessageDAO handleMess = new MessageDAO();
    DefaultListModel listModel;
    ArrayList<ComboItem> onlineFriendList = new ArrayList<>();
    int BUFF_SIZE = 512;
    
    int hostPort = randomRange(1260, 1460);
    /**
     * Creates new form frmChat
     */
    public frmChat(String username) {
        this.username = username;
        initComponents();
        
//        set form center of screen
        setLocationRelativeTo(null);
    }
    
    class SendFileThread extends Thread {
        String toIp;
        int toPort;
        int hostPort;
        String filePath;
        String fileName;

        public SendFileThread(String IP, int toPort, int hostPort, String filePath, String fileName) {
            this.toIp = IP;
            this.toPort = toPort;
            this.hostPort = hostPort;
            this.filePath = filePath;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            TransferFile fileSocket = new TransferFile(this.toIp, this.toPort, this.hostPort);
            fileSocket.sendFile(this.filePath, this.fileName);
        }
        
    }
    
    class ReceiveFileThread extends Thread {
        FileOutputStream fileOutput;
        String fileName;

        public ReceiveFileThread(String fileName) {
            this.fileName = fileName;
            try {
                fileOutput = new FileOutputStream("./" + this.fileName);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(frmChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        @Override
        public void run() {
            while(true) {
                if(fileSocket.isReceivingFile) {
                    try {
                        byte[] buff = new byte[BUFF_SIZE];
                        buff = fileSocket.receiveByte();
                        String data = new String(buff, 0, fileSocket.receiveByteLength);
                        
                        if(data.trim().equals("ENDFILE")) {
                            fileOutput.close();
                            fileSocket.isReceivingFile = false;
                            listModel.addElement("Received '" + fileName + "'");
                            return;
                        } else {
                            fileOutput.write(buff, 0, fileSocket.receiveByteLength);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    
    class ReceiveMessageThread extends Thread {
        @Override
        public void run() {
            
            while(true) {
                String pkg = messageSocket.receiveMessage();
                if(pkg.equals("")) return;
                
                System.out.println("MESSAGE: " + pkg);
                String sub = pkg.substring(0, 4);
                if(sub.equals("FILE")) {
                    String fileName = pkg.substring(5, pkg.length());
                    listModel.addElement("Receiving file '" + fileName + "'");
                    fileSocket.isReceivingFile = true;
                    Thread t = new Thread(new ReceiveFileThread(fileName));
                    t.start();
                } else {
                    String[] tmp = pkg.split("[:]");
                    String message = tmp[1];
                    listModel.addElement(message);
                }
                listMessage.setModel(listModel);
            }
        }
    }
    
    class ComboItem {
        private String fullname;
        private String username;
        private String ip;

        public ComboItem(String username, String fullname, String ip) {
            this.username = username;
            this.fullname = fullname;
            this.ip = ip;
        }

        @Override
        public String toString() { return fullname; }
        public String getUsername() { return username; }
        public String getIp() { return ip; }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbbFriendList = new javax.swing.JComboBox<>();
        txtMessage = new javax.swing.JTextField();
        btnSendMessage = new javax.swing.JButton();
        btnAttach = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listMessage = new javax.swing.JList<>();
        pnHeader = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtPort = new javax.swing.JTextField();
        txtToPort = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        menubar = new javax.swing.JMenuBar();
        menuAccount = new javax.swing.JMenu();
        menuEdit = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Chat to:");

        cbbFriendList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtMessage.setText("Message");

        btnSendMessage.setText("Send");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });

        btnAttach.setText("Attach");
        btnAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttachActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(listMessage);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAttach)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbbFriendList, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtMessage, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbbFriendList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSendMessage)
                    .addComponent(btnAttach))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Droid Serif", 2, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setText("Chat!");
        pnHeader.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Droid Serif", 2, 18)); // NOI18N
        jLabel2.setText("Yahoo");
        pnHeader.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, -1, -1));

        txtPort.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPort.setText("1260");

        txtToPort.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtToPort.setText("1261");

        jLabel4.setText("To port:");

        jLabel5.setText("Host port:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(3, 3, 3)
                .addComponent(txtToPort, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtToPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuAccount.setText("Account");
        menubar.add(menuAccount);

        menuEdit.setText("Edit");
        menubar.add(menuEdit);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(pnHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateFriendList() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        if(username == null){
            model.addElement("Log in to see list");
        } else {
            ResultSet rs = handle.getOnline();
            ComboItem item;
            try {
                while(rs.next()) {
                    item = new ComboItem(rs.getString("username"), rs.getString("fullname"), rs.getString("ip"));
                    model.addElement(item);
                    onlineFriendList.add(item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(frmChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        cbbFriendList.setModel(model);
    }
    
    private void updateMenubar() {
        menuAccount.setText(fullname != null ?  fullname : "Account");
        if(username == null) {
            JMenuItem loginMenu = new JMenuItem("Login");
            loginMenu.addActionListener((e) -> {
                new frmLogIn(null).show();
                this.dispose();
            });
            menuAccount.add(loginMenu);
            
            JMenuItem signupMenu = new JMenuItem("Signup");
            signupMenu.addActionListener((e) -> {
                new frmSignUp(null).show();
                this.dispose();
            });
            menuAccount.add(signupMenu);
        } else {
            JMenuItem loginMenu = new JMenuItem("Login as another account");
            loginMenu.addActionListener((e) -> {
                new frmLogIn(username).show();
                this.dispose();
            });
            menuAccount.add(loginMenu);
            
            JMenuItem changePasswordMenu = new JMenuItem("Change password");
            changePasswordMenu.addActionListener((e) -> {
                new frmChangePassword(username).show();
            });
            menuAccount.add(changePasswordMenu);
        }
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener((e) -> {
            handle.setOnlineState(username, "0");
            System.exit(0);
        });
        menuAccount.add(exitMenu);
        
        JMenuItem saveMessage = new JMenuItem("Save message");
        saveMessage.addActionListener((e) -> {
            for(int i = 0; i< listMessage.getModel().getSize();i++){
                try {
                    System.out.println(listMessage.getModel().getElementAt(i));
                    handleMess.saveMessage(new Message(this.username, listMessage.getModel().getElementAt(i)));
                } catch (SQLException ex) {
                    Logger.getLogger(frmChat.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(frmChat.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Message saved to database.");
        });
        menuEdit.add(saveMessage);
        
        JMenuItem showMessage = new JMenuItem("Show message");
        menuEdit.add("Show message");
        
    }
    
    private static int randomRange(int min, int max) {
        if (min >= max) {
                throw new IllegalArgumentException("Max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        listModel = new DefaultListModel();
        
        if(username != null) {
            fullname = handle.getFullname(username);
//            update online state
            handle.setOnlineState(username, "1");
            
//            hostPort = Integer.parseInt(txtPort.getText());
            messageSocket.openPort(hostPort);
            fileSocket.openPort(hostPort + 1);
            txtPort.setText(String.valueOf(hostPort));
            
            Thread revMessThread = new Thread(new ReceiveMessageThread());
            revMessThread.start();
        }
        
        updateFriendList();
        updateMenubar();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if(username != null)
            handle.setOnlineState(username, "0");
    }//GEN-LAST:event_formWindowClosing

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        String hostIp = "";
        try {
            
            hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(frmChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        String mess = txtMessage.getText();
        String message = hostIp + ":" + mess;
        if(message.equals("")){
            return;
        }
        int toPort = Integer.parseInt(txtToPort.getText());
        
//        get receiver IP
        Object selected = cbbFriendList.getSelectedItem();
//        String IP = ((ComboItem)selected).getIp();
        
        listModel.addElement("Me: " + mess);
        listMessage.setModel(listModel);
        
        String IP = "127.0.0.1";
        messageSocket.sendMessage(IP, toPort, message);
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void btnAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttachActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(this);
        int toPort = Integer.parseInt(txtToPort.getText());
//        String IP = ((ComboItem)selected).getIp();
        String IP = "127.0.0.1";

        String filePath = chooser.getSelectedFile().getPath();
        String fileName = chooser.getSelectedFile().getName();
        
        listModel.addElement("Me: Send file '" + fileName + "'");
        listMessage.setModel(listModel);
        
        SendFileThread sendFileThread = new SendFileThread(IP, toPort, hostPort, filePath, fileName);
        sendFileThread.start();
    }//GEN-LAST:event_btnAttachActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmChat(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAttach;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JComboBox<String> cbbFriendList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> listMessage;
    private javax.swing.JMenu menuAccount;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtToPort;
    // End of variables declaration//GEN-END:variables
}
