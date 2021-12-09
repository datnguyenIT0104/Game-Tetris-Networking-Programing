package tcp.client.view.group;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Friend;
import model.Group;
import model.Joining;
import model.ObjectWrapper;
import model.TournamentUser;
import model.User;
import tcp.client.control.ClientCtr;
import tcp.client.view.general.HomeFrm;
import tcp.client.view.match.ChallengeFrm;

/**
 *
 * @author DatIT
 */
public class PlayInGroupFrm extends javax.swing.JFrame {

    private ClientCtr myControl;
    private User myAccount;
    private ArrayList<Joining> list;
    private DefaultTableModel model;
    private Group myGroup;
    private ArrayList<User> listUsersOnline;

    public PlayInGroupFrm(ClientCtr myControl, User myAccount, Group myGroup) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;
        this.myGroup = myGroup;
        list = new ArrayList<>();
        listUsersOnline = ((HomeFrm) myControl.getForm()).getListUsersOnline();

        this.pack();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        initTable();
        
    }
    
    public void initTable() {
        
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_INFO_OF_PLAYER_IN_GROUP, myGroup));
        model = (DefaultTableModel) tblMember.getModel();

        labGroupName.setText(myGroup.getName());

        tblMember.setModel(model);
        txtMessage.setText("");
        txaBoard.setText("");
        labUsername.setText("You: " + myAccount.getUsername());
    }

    private void fillTable() {
        model.setRowCount(0);
        Joining myJoining = new Joining();
        boolean isKicked = true;
        for (Joining item : list) {
            User u = item.getUser();
            String status = "";
            for (User user : listUsersOnline) {
                if (user.getId() == u.getId()) {
                    if (user.getStatus() == User.ONLINE) {
                        status = "Online";
                    } else {
                        status = "Playing";
                    }

                    break;
                }
            }
            model.addRow(new Object[]{
                item.getUser().getUsername(), item.getRoleInGroup(), item.getScore(), status
            });
            if (u.getId() == myAccount.getId()) {
                myJoining = item;
                isKicked = false;
            }
        }
        if( isKicked){
            kickByAdmin();
            return;
        }
        // neu la thanh vien thi khong the kick thanh vien khac
        if ( myJoining.getRoleInGroup() != null && myJoining.getRoleInGroup().equals("member")) {
            btnKick.setVisible(false);
            btnLeaveGroup.setEnabled(true);
        }else{
            btnKick.setVisible(true);
            btnLeaveGroup.setEnabled(false);
        }
        // neu bi kich thi thong bao
        
        
        model.fireTableDataChanged();
    }

    public void kickByAdmin() throws HeadlessException {
        JOptionPane.showMessageDialog(this, "You have been kicked by admin");
        ((HomeFrm) myControl.getForm()).setVisible(true);
        this.dispose();
        return;
    }
    public void receiveDataProcessing(ObjectWrapper ow) {
        if (ow.getData() instanceof ArrayList<?>) {
            list = (ArrayList<Joining>) ow.getData();
            fillTable();
            
            myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MESSAGE_OF_GROUP, myGroup));
        }else {
            if( ow.getPerformative() == ObjectWrapper.REPLY_SEND_INVITATION_GROUP){
                
                if( ow.getData().equals("ok"))
                    JOptionPane.showMessageDialog(this, "Send successfully!");
                else
                    JOptionPane.showMessageDialog(this, "You invited " + ((Friend)ow.getData()).getName() + " before.\n"
                            + "Wait " +((Friend)ow.getData()).getName() + " reply!" );
            }
        }
    }
    public void updateBoard(String string){
        txaBoard.setText(string);
    }

    public ArrayList<User> getListUsersOnline() {
        return listUsersOnline;
    }

    public void setListUsersOnline(ArrayList<User> listUsersOnline) {
        this.listUsersOnline = listUsersOnline;
        if( list.size() != 0)
            fillTable();
    }

    public Group getMyGroup() {
        return myGroup;
    }

    public void setMyGroup(Group myGroup) {
        this.myGroup = myGroup;
    }

    public JTextArea getTxaBoard() {
        return txaBoard;
    }

    public void setTxaBoard(JTextArea txaBoard) {
        this.txaBoard = txaBoard;
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labGroupName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMember = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaBoard = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        btnLeaveGroup = new javax.swing.JButton();
        btnInvite = new javax.swing.JButton();
        btnKick = new javax.swing.JButton();
        btnBackHome = new javax.swing.JButton();
        btnChallenge = new javax.swing.JButton();
        labUsername = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Group");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        labGroupName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labGroupName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labGroupName.setText("Group");

        tblMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "USERNAME", "ROLE", "SCORE", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMember.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMemberMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMember);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Chat:");

        txaBoard.setEditable(false);
        txaBoard.setColumns(20);
        txaBoard.setRows(5);
        jScrollPane2.setViewportView(txaBoard);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnLeaveGroup.setText("Leave Group");
        btnLeaveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveGroupActionPerformed(evt);
            }
        });

        btnInvite.setText("Invite Friend");
        btnInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInviteActionPerformed(evt);
            }
        });

        btnKick.setText("Kick");
        btnKick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKickActionPerformed(evt);
            }
        });

        btnBackHome.setText("Back Home");
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        btnChallenge.setText("Challenge");
        btnChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChallengeActionPerformed(evt);
            }
        });

        labUsername.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labUsername.setText("You:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnChallenge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLeaveGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnInvite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBackHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(labUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChallenge)
                    .addComponent(btnInvite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLeaveGroup)
                    .addComponent(btnBackHome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(btnKick))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labGroupName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labGroupName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackHomeActionPerformed
        ((HomeFrm) myControl.getForm()).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackHomeActionPerformed

    private void tblMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberMouseClicked
        listUsersOnline = ((HomeFrm) myControl.getForm()).getListUsersOnline();

        
    }//GEN-LAST:event_tblMemberMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ((HomeFrm) myControl.getForm()).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void btnLeaveGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveGroupActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure to leave this group?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {// lua chon yess

            Joining joining = new Joining();
            for (Joining item : list) {
                if (item.getUser().getUsername().equalsIgnoreCase(myAccount.getUsername())) {
                    joining = item;

                    break;
                }
            }
            Group sendGroup = new Group();
            sendGroup.setId(myGroup.getId());
            sendGroup.getListJoining().add(joining);
            
            ((HomeFrm) myControl.getForm()).setVisible(true);
            ((HomeFrm) myControl.getForm()).leveaGroup(myGroup);
            myControl.sendData(new ObjectWrapper(ObjectWrapper.LEAVE_GROUP, sendGroup));
            this.dispose();
        }
    }//GEN-LAST:event_btnLeaveGroupActionPerformed

    private void btnKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKickActionPerformed
        int selected = tblMember.getSelectedRow();
        if (selected >= 0) {
            String username = (String) model.getValueAt(selected, 0);
            if( username.equals(myAccount.getUsername()))
                return;
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure to kick " + username + "?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {// lua chon yess

                Joining joining = new Joining();
                for (Joining item : list) {
                    if (item.getUser().getUsername().equalsIgnoreCase(username)) {
                        joining = item;

                        break;
                    }
                }
                Group sendGroup = new Group();
                sendGroup.setId(myGroup.getId());
                sendGroup.getListJoining().add(joining);
                
                myControl.sendData(new ObjectWrapper(ObjectWrapper.KICK_OUT_GROUP, sendGroup));

                list.remove(joining);
                fillTable();
            }
        }
    }//GEN-LAST:event_btnKickActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String message = "\n " + myAccount.getUsername() +": "+ txtMessage.getText();
        txtMessage.setText("");
        
        txaBoard.setText( txaBoard.getText() + message);
        
        // gui len server
        myGroup.setMessage(message);
        myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND_MESSAGE_OF_GROUP, myGroup));
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInviteActionPerformed
        myGroup.setNumMember(list.size());
        ( new FriendFrm(myControl, myAccount, myGroup, list)).setVisible(true);
    }//GEN-LAST:event_btnInviteActionPerformed

    private void btnChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChallengeActionPerformed
        int selected = tblMember.getSelectedRow();
        if (selected >= 0) {
            String username = (String) model.getValueAt(selected, 0);
            boolean online = false;
            boolean playing = false;
            // kiem tra click vao ten cua minh hay k?
            if (username.equals(myAccount.getUsername())) {
                return;
            }

            // kiem tra co dang online hay k?
            User userSend = new User();
            for (User user : listUsersOnline) {
                if (user.getUsername().equals(username)) {
                    userSend = user;
                    online = true;
                    
                    if( user.getStatus() == User.PLAYING)
                        playing = true;
                    break;
                }
            }
            if (!online) {
                JOptionPane.showMessageDialog(this, username + " not online now");
                return;
            }
            if( playing){
                JOptionPane.showMessageDialog(this, username + " is playing now");
                return;
            }

            ObjectWrapper funAc = null;
            for (int i = 0; i < myControl.getMyFuntion().size(); i++) {
                ObjectWrapper fun = myControl.getMyFuntion().get(i);
                if (fun.getData() instanceof ChallengeFrm) {
                    funAc = fun;
//                break;
                }
            }
            if (funAc != null) {
                myControl.getMyFuntion().remove(funAc);
            }

//            ((HomeFrm) myControl.getForm()).setVisible(true);
            ChallengeFrm challengeFrm = new ChallengeFrm(myControl, userSend, myAccount, myGroup);

//            this.setVisible(false);
        }
    }//GEN-LAST:event_btnChallengeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnChallenge;
    private javax.swing.JButton btnInvite;
    private javax.swing.JButton btnKick;
    private javax.swing.JButton btnLeaveGroup;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labGroupName;
    private javax.swing.JLabel labUsername;
    private javax.swing.JTable tblMember;
    private javax.swing.JTextArea txaBoard;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
