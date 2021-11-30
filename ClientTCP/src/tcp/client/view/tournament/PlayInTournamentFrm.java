package tcp.client.view.tournament;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ObjectWrapper;
import model.Ranking;
import model.Tournament;
import model.TournamentUser;
import model.User;
import tcp.client.control.ClientCtr;
import tcp.client.view.general.HomeFrm;
import tcp.client.view.match.ChallengeFrm;

/**
 *
 * @author DatIT
 */
public class PlayInTournamentFrm extends javax.swing.JFrame {

    private DefaultTableModel model;
    private Tournament myTournament;
    private User myAccount;
    private ClientCtr myControl;
//    private ArrayList<Tournament> listTournament;
    private ArrayList<TournamentUser> listPlayer;
    
    private ArrayList<User> listUsersOnline;
    
    public PlayInTournamentFrm() {
        initComponents();
    }

    public PlayInTournamentFrm(User myAccount, ClientCtr myControl, Tournament myTournament) {
        initComponents();
        this.myAccount = myAccount;
        this.myControl = myControl;
//        this.listTournament = listTournament;
        this.myTournament = myTournament;

        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        initTable();
        updatePropertisHiden();
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_INFO_OF_PLAYER_IN_TOURNAMENT, this));
        
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_INFO_OF_PLAYER_IN_TOURNAMENT, myTournament));
    }
    public void updatePropertisHiden(){
       
       
        
    }
    private void initTable() {
        // dat cac thong tin
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        labNameTournament.setText(myTournament.getName());
        labNumOPlayer.setText("Number of Player: " + myTournament.getListTournamentUsers().size());
        labEndTime.setText("End time: " + sdf.format(myTournament.getEndDate()));

        model = (DefaultTableModel) tblTournamentUser.getModel();

        updatePropertisHiden();

        listPlayer = new ArrayList<>();
        
        int index = 1;
        for (TournamentUser tu : listPlayer) {
            model.addRow(new Object[]{
                index++, tu.getUser().getId(), tu.getUser().getUsername(), tu.getUser().getName(), tu.getTotalScore()
            });
        }

        tblTournamentUser.setModel(model);
        // lay thong tin nguoi choi tu server
    }

    public void fillTable() {
        model.setRowCount(0);
        int index = 1;
        for (TournamentUser tu : listPlayer) {
            model.addRow(new Object[]{
                index++, tu.getUser().getId(), tu.getUser().getUsername(), tu.getUser().getName(), tu.getTotalScore()
            });
        }
        model.fireTableDataChanged();
    }
    public void receiveDataProcessing(ObjectWrapper ow){
        if( ow.getPerformative() == ObjectWrapper.REPLY_GET_INFO_OF_PLAYER_IN_TOURNAMENT){
            
            listPlayer = (ArrayList<TournamentUser>) ow.getData();
            
            fillTable();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labNameTournament = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTournamentUser = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        labNumOPlayer = new javax.swing.JLabel();
        labEndTime = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        labNameTournament.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labNameTournament.setText("Name Tournament");

        tblTournamentUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "INDEX", "ID", "USERNAME", "NAME", "TOTAL SCORE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTournamentUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTournamentUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTournamentUser);

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        labNumOPlayer.setText("Number of Player:");

        labEndTime.setText("End time:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labNameTournament)
                .addGap(166, 166, 166))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labNumOPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labNameTournament)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labNumOPlayer)
                    .addComponent(labEndTime))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBack)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ObjectWrapper fun = null;
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if( activeFun.getData() instanceof JoinTournamentFrm){
                fun = activeFun;
                
            }
        }
        if( fun!= null)
            myControl.getMyFuntion().remove(fun);
        
        JoinTournamentFrm joinTournamentFrm = new JoinTournamentFrm(myControl, myAccount);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void tblTournamentUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTournamentUserMouseClicked
        listUsersOnline = ((HomeFrm) myControl.getForm()).getListUsersOnline();
        
        int selected = tblTournamentUser.getSelectedRow();
        if( selected >= 0){
            String username = (String) model.getValueAt(selected, 2);
            boolean online = false;
            boolean playing = false;
            // kiem tra click vao ten cua minh hay k?
            if( username.equals(myAccount.getUsername()))
                return;
            
            // kiem tra co dang online hay k?
            User userSend = new User();
            for (User user : listUsersOnline) {
                if( user.getUsername().equals(username)){
                    userSend = user;
                    online = true;
                    
                    if( user.getStatus() == User.PLAYING)
                        playing = true;
                    break;
                }
            }
            if( !online){
                JOptionPane.showMessageDialog(this, username + " not online now");
                return;
            }
            if(playing){
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
            if (funAc != null)
                myControl.getMyFuntion().remove(funAc);
            
            
            ChallengeFrm challengeFrm = new ChallengeFrm(myControl, userSend, myAccount, myTournament);
            this.dispose();
        }
    }//GEN-LAST:event_tblTournamentUserMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labEndTime;
    private javax.swing.JLabel labNameTournament;
    private javax.swing.JLabel labNumOPlayer;
    private javax.swing.JTable tblTournamentUser;
    // End of variables declaration//GEN-END:variables
}
