package tcp.client.view.match;

//import drawLine.GameForm;
import game.view.tetrisgame.GameFormClient;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.Match;
import model.ObjectWrapper;
import model.User;
import tcp.client.control.ClientCtr;
import tcp.client.view.general.HomeFrm;
import tcp.client.view.group.PlayInGroupFrm;

/**
 *
 * @author DatIT
 */
public class ReceiveChallengeFrm extends JFrame {

    private ClientCtr myControl;
    private User myAccount;
    private Match match;
    

    public ReceiveChallengeFrm(ClientCtr myControl, User myAccount, Match match) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;
        this.match = match;
        
        initForm();
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    private void initForm(){
        User sender = match.getListResult().get(1).getUser();
        labUsersend.setText("Do you want to play with " + sender.getName() + "?");
        txaMessage.setText(match.getNote());
        labMode.setText("Mode: " + match.getMode().getName());
        
        if( match.getTournament() != null){
            labTournamentORGroup.setText("Tournament: " + match.getTournament().getName());
        }else if( match.getGroup() != null)
            labTournamentORGroup.setText("Group: " + match.getGroup().getName());
        else
            labTournamentORGroup.hide();
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        labMode = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaMessage = new javax.swing.JTextArea();
        labUsersend = new javax.swing.JLabel();
        btnAccept = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        labTournamentORGroup = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Receiving Challenge");

        labMode.setText("Mode:");

        jLabel3.setText("Message:");

        txaMessage.setEditable(false);
        txaMessage.setColumns(20);
        txaMessage.setRows(5);
        jScrollPane1.setViewportView(txaMessage);

        labUsersend.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labUsersend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labUsersend.setText("play");

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        labTournamentORGroup.setText("Tournament or Group");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(labMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(btnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnReject, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 31, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addComponent(labUsersend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labTournamentORGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labUsersend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(labTournamentORGroup)
                .addGap(18, 18, 18)
                .addComponent(labMode)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept)
                    .addComponent(btnReject))
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        
        // dong y
        User userSendCh = match.getListResult().get(1).getUser();
        boolean isOnline = false;
        for (User user : ( (HomeFrm)myControl.getForm()).getListUsersOnline() ) {
            if( user.getId() == userSendCh.getId()){
                isOnline = true;
                if( user.getStatus() == User.PLAYING){
                    JOptionPane.showMessageDialog(this, userSendCh.getName() + " is playing\nYou can't play with " + userSendCh.getName() + " now");
                    
                    this.dispose();
                }
            }
        }
        if( !isOnline){
            
            JOptionPane.showMessageDialog(this, userSendCh.getName()+ " is offline");
            this.dispose();
        }
//        myControl.sendData(new ObjectWrapper(ObjectWrapper.CHANGE_STATUS, User.PLAYING));

        Random r = new Random();
        int[] random = new int[10000];
        for (int i = 0; i < random.length; i++) {
            random[i] = r.nextInt(7);
            if( i != 0&& random[i] == random[i-1] ){
                while( random[i] == random[i-1]){
                    random[i] = r.nextInt(7);
                }
            }
        }
        match.setRandomBlock(random);
        myControl.sendData(new ObjectWrapper(ObjectWrapper.ACCEPT_CHALLENGE_COMMUNICATE, match));
        
//        new GameForm(myControl, match, myAccount).setVisible(true);
        new GameFormClient(myControl, match, myAccount).setVisible(true);

        ((HomeFrm)myControl.getForm()).setVisible(false);
        if( ((HomeFrm)myControl.getForm()).getPlayInGroupFrm() != null)
            ((HomeFrm)myControl.getForm()).getPlayInGroupFrm().setVisible(false);
        
        this.dispose();
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
//        match.setStatusChallenge( Match.REJEST);
//        myControl.remoteRemoveChallenge(match);
        this.dispose();
    }//GEN-LAST:event_btnRejectActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        match.setStatusChallenge( Match.REJEST);
//        myControl.remoteRemoveChallenge(match);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnReject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labMode;
    private javax.swing.JLabel labTournamentORGroup;
    private javax.swing.JLabel labUsersend;
    private javax.swing.JTextArea txaMessage;
    // End of variables declaration//GEN-END:variables
}
