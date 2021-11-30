package tcp.client.view.general;

import tcp.client.view.match.ChallengeFrm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.Friend;
import model.ObjectWrapper;
import model.Ranking;
import model.User;
import tcp.client.control.ClientCtr;

/**
 *
 * @author DatIT
 */
public class PlayerDetailsFrm extends JFrame {
    private ClientCtr myControl;
    private Ranking rank;
    private ArrayList<Friend> list;
    private User myAccount;
    private Friend friend;
    private ArrayList<User> listFriendRequest;
    private ArrayList<User> listUsersOnline;
    
    public PlayerDetailsFrm(ClientCtr myControl, Ranking rank, User myAccount, ArrayList<User> listFriendRequest, ArrayList<User> listUsersOnline) {
        initComponents();
        this.myControl = myControl;
        this.rank = rank;
        this.list = myAccount.getListFriend();
        this.myAccount = myAccount;
        this.listFriendRequest = listFriendRequest;
        this.listUsersOnline = listUsersOnline;
        // dat cac thuoc tinh
        setProperties();
        
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_REQUEST_FRIEND, this));
    }
    
    public PlayerDetailsFrm(ClientCtr myControl, Friend friend, User myAccount, ArrayList<User> listFriendRequest, ArrayList<User> listUsersOnline) {
        initComponents();
        this.myControl = myControl;
        this.friend = friend;
        this.myAccount = myAccount;
        this.listFriendRequest = listFriendRequest;
        this.listUsersOnline = listUsersOnline;
        // dat cac thuoc tinh
        setPropertiesForFriend();
        
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_REQUEST_FRIEND, this));
    }
    public void setProperties(){
        // bat lai cac nut
        btnAddFriend.setEnabled(true);
        btnChallenge.setEnabled(true);
        btnUnfriend.setEnabled(true);
        btnChatPrivate.setEnabled(true);
        
        // set lai danh sach ban be
        this.list = myAccount.getListFriend();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        labUsername.setText(rank.getUsername());
        labName.setText( rank.getName());
        labRole.setText(rank.getRole());
        
        labEmail.setText("");
        labBirthday.setText("");
        
        btnUnfriend.setEnabled(false);
        
        // day la thong tin cua minh
        if( myAccount.getId() == rank.getId()){
            btnAddFriend.setEnabled(false);
            btnChallenge.setEnabled(false);
            labEmail.setText(rank.getEmail());
            labBirthday.setText( sdf.format( rank.getBirthday()));
        }
        // neu la ban be thi khong cho ket ban ma cho huy ket ban
        boolean isFriend = false;
        for (Friend f : list) {
            if( rank.getId() == f.getId()){
                btnAddFriend.setEnabled(false);
                btnUnfriend.setEnabled(true);
                labEmail.setText(rank.getEmail());
                labBirthday.setText( sdf.format( rank.getBirthday()));
                isFriend = true;
                break;
            }
        }
        
        // TH nguoi do da gui loi ket ban nhung minh chua tl
        for (User user : listFriendRequest) {
            if( user.getId() == rank.getId() && user.getListFriend().get(0).getId() == myAccount.getId() &&
                    user.getListFriend().get(0).getPerformative() == Friend.HAS_NOT_REPLIED_YET){
                btnAddFriend.setEnabled(false);
                break;
            }
        }
        // kiem tra xem co dang online hay dang playing
        boolean isExist = false;
        for (User user : listUsersOnline) {
            if( user.getId() == rank.getId()){
                isExist = true;
                if( user.getStatus() == User.PLAYING){
                    btnChallenge.setEnabled(false);
                    break;
                }
            }
        }
        if( !isExist)  btnChallenge.setEnabled(false);
        enableChallenge();
        
        // 
        if( myAccount.getRole().equalsIgnoreCase("player"))
            btnBan.hide();
        // neu khong phai la ban thi k cho chat
        if( !isFriend)
            btnChatPrivate.setEnabled(false);
        else{
            friend = new Friend();
            friend.setId(rank.getId());
            friend.setUsername(rank.getUsername());
            friend.setName(rank.getName());
            
        }
    }
    public void setPropertiesForFriend(){
        // bat lai cac nut
        btnAddFriend.setEnabled(true);
        btnChallenge.setEnabled(true);
        btnUnfriend.setEnabled(true);
        btnChatPrivate.setEnabled(true);
        this.list = myAccount.getListFriend();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        labUsername.setText(friend.getUsername());
        labName.setText( friend.getName());
        labRole.setText(friend.getRole());
        labEmail.setText(friend.getEmail());
        labBirthday.setText( sdf.format( friend.getBirthday()));
        
        
        btnAddFriend.setEnabled(false);
        btnUnfriend.setEnabled(true);
        
        // kiem tra xem co dang online hay dang playing
        boolean isExist = false;
        for (User user : listUsersOnline) {
            if( user.getId() == friend.getId()){
                isExist = true;
                if( user.getStatus() == User.PLAYING){
                    btnChallenge.setEnabled(false);
                    break;
                }
            }
        }
        if( !isExist)  btnChallenge.setEnabled(false);
        enableChallenge();
        
    }
    private void enableChallenge(){                                     // neu dang choi thi khong dk thach dau
        for (User user : listFriendRequest) {
            if( user.getId() == myAccount.getId() && user.getStatus() == User.PLAYING){
                btnChallenge.setEnabled(false);
                return;
            }
        }
    }
    public void receiveDataProcessing(ObjectWrapper ow){
        if( ow.getData().equals("ok")){
            JOptionPane.showMessageDialog(this, "Send request successfully");
            
            this.dispose();
        }
    }

    public void setMyControl(ClientCtr myControl) {
        this.myControl = myControl;
    }

    public void setRank(Ranking rank) {
        this.rank = rank;
    }

    public void setMyAccount(User myAccount) {
        this.myAccount = myAccount;
    }

    public void setListFriendRequest(ArrayList<User> listFriendRequest) {
        this.listFriendRequest = listFriendRequest;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public ArrayList<User> getListUsersOnline() {
        return listUsersOnline;
    }

    public void setListUsersOnline(ArrayList<User> listUsersOnline) {
        this.listUsersOnline = listUsersOnline;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labName = new javax.swing.JLabel();
        labRole = new javax.swing.JLabel();
        labBirthday = new javax.swing.JLabel();
        labEmail = new javax.swing.JLabel();
        btnChallenge = new javax.swing.JButton();
        btnAddFriend = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        btnUnfriend = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        labUsername = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        btnBan = new javax.swing.JButton();
        btnChatPrivate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Detail Player");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Player");

        jLabel2.setText("Name:");

        jLabel3.setText("Role:");

        jLabel4.setText("Birthday:");

        jLabel5.setText("Email:");

        labName.setText("name");

        labRole.setText("role");

        labBirthday.setText("birthday");

        labEmail.setText("email");

        btnChallenge.setText("Challenge ");
        btnChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChallengeActionPerformed(evt);
            }
        });

        btnAddFriend.setText("Add Friend");
        btnAddFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFriendActionPerformed(evt);
            }
        });

        btnUnfriend.setText("Unfriend");
        btnUnfriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnfriendActionPerformed(evt);
            }
        });

        jLabel6.setText("Username:");

        labUsername.setText("username");

        btnBan.setText("Ban");

        btnChatPrivate.setText("Chat Private");
        btnChatPrivate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChatPrivateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(labName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(labEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labBirthday, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(labUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnChallenge, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                    .addComponent(btnBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnAddFriend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnChatPrivate, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(51, 51, 51)
                                .addComponent(btnUnfriend, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labRole, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labUsername))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(labName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labRole)
                    .addComponent(jLabel3))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(labBirthday))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(labEmail))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChallenge)
                    .addComponent(btnUnfriend)
                    .addComponent(btnAddFriend))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBan)
                    .addComponent(btnChatPrivate))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFriendActionPerformed
        for (User user : listFriendRequest) {                                   // da gui yc kb roi
            if( myAccount.getId() == user.getId() && user.getListFriend().get(0).getId() == rank.getId()){
                
                return;
            }
        }
        // dong goi thuoc tinh nguoi gui yeu cau kb
        User userTemp = new User();
        userTemp.setId(myAccount.getId());
        userTemp.setUsername(myAccount.getUsername());
        userTemp.setName(myAccount.getName());
        userTemp.setBirthday(myAccount.getBirthday());
        userTemp.setRole(myAccount.getRole());
        userTemp.setEmail(myAccount.getEmail());
        
        // dong goi tt nguoi nhan yc kb
        Friend addFriend = new Friend();
        addFriend.setId(rank.getId());
        addFriend.setUsername(rank.getUsername());
        addFriend.setName(rank.getName());
        addFriend.setBirthday(rank.getBirthday());
        addFriend.setRole(rank.getRole());
        addFriend.setEmail(rank.getEmail());
        
        addFriend.setPerformative(Friend.HAS_NOT_REPLIED_YET);
        userTemp.getListFriend().add(addFriend);
        myControl.sendData(new ObjectWrapper(ObjectWrapper.REQUEST_FRIEND, userTemp));
        
    }//GEN-LAST:event_btnAddFriendActionPerformed

    private void btnUnfriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnfriendActionPerformed
        String username = labUsername.getText();
        String name = labName.getText();
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure to unfriend with " + name +" ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if( choice == JOptionPane.NO_OPTION) return;
        User userTemp = new User();
        userTemp.setId( myAccount.getId());
        for (Friend f : list) {
            if( f.getUsername().equals(username)){
                userTemp.getListFriend().add(f);
                myControl.sendData(new ObjectWrapper(ObjectWrapper.UNFRIEND, userTemp));
                this.dispose();
                return;
            }
        }
    }//GEN-LAST:event_btnUnfriendActionPerformed

    private void btnChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChallengeActionPerformed
        // lay user dang hien thi
        String username = labUsername.getText().trim();
        User userSend = new User();
        if( username.equals( rank.getUsername())){
            userSend.setId(rank.getId());
            userSend.setUsername(rank.getUsername());
            userSend.setName(rank.getName());
        }else if( username.equals(friend.getUsername())){
            userSend.setId(friend.getId());
            userSend.setUsername(friend.getUsername());
            userSend.setName(friend.getName());
        }
        // xoa form ChallengeFrm
        ObjectWrapper funAc = null;
        for (int i = 0; i < myControl.getMyFuntion().size(); i++) {
            ObjectWrapper fun = myControl.getMyFuntion().get(i);
            if( fun.getData() instanceof ChallengeFrm){
                funAc = fun;
//                break;
            }
        }
        if( funAc != null)
            myControl.getMyFuntion().remove(funAc);
        // neu van con thi cho no xuat hien
        
        ChallengeFrm cf = new ChallengeFrm(myControl, userSend, myAccount);
        cf.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnChallengeActionPerformed

    private void btnChatPrivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChatPrivateActionPerformed
        this.setVisible(false);
        ChatPrivateFrm chatPrivateFrm = new ChatPrivateFrm(myAccount, myControl, friend);
        
        chatPrivateFrm.setVisible(true);
    }//GEN-LAST:event_btnChatPrivateActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFriend;
    private javax.swing.JButton btnBan;
    private javax.swing.JButton btnChallenge;
    private javax.swing.JButton btnChatPrivate;
    private javax.swing.JButton btnUnfriend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel labBirthday;
    private javax.swing.JLabel labEmail;
    private javax.swing.JLabel labName;
    private javax.swing.JLabel labRole;
    private javax.swing.JLabel labUsername;
    // End of variables declaration//GEN-END:variables
}
