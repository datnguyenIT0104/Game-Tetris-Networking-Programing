package tcp.client.view.tournament;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author DatIT
 */
public class JoinTournamentFrm extends javax.swing.JFrame {

    private ClientCtr myControl;
    private User myAccount;
    private ArrayList<Tournament> listTournaments;
    private int idTIndex;
    
    private DefaultTableModel model;

    public JoinTournamentFrm(ClientCtr myControl, User myAccount) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;

        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_JOIN_TO_THE_TOURNAMENT, this));
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_TOURNAMENT, this));
        
        initTable();
    }
    public void getDataFromServer(){
        Date date = new Date();
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_TOURNAMENT, date));
    }
    private void initTable() {
        getDataFromServer();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new Object[]{
            "INDEX", "ID", "NAME", "END DATE", "NUMBER OF PLAYER"
        });
        
        listTournaments = new ArrayList<>();
        int index = 1;
        for (Tournament tournament : listTournaments) {
            model.addRow(new Object[]{
                index++, tournament.getId(), tournament.getName(), sdf.format(tournament.getEndDate()), tournament.getListTournamentUsers().size()
            });
        }
        tblTournament.setModel(model);

    }

    private void fillTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        model.setRowCount(0);
        int index = 1;
        for (Tournament tournament : listTournaments) {
            model.addRow(new Object[]{
                index++,tournament.getId(), tournament.getName(), sdf.format(tournament.getEndDate()), tournament.getListTournamentUsers().size()
            });
        }
        model.fireTableDataChanged();
    }
    public void receiveDataProcessing(ObjectWrapper ow){
        if( ow.getPerformative() == ObjectWrapper.REPLY_GET_TOURNAMENT){
           
            if (ow.getData() instanceof ArrayList<?>) {
                listTournaments = (ArrayList<Tournament>) ow.getData();
                fillTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error when get information of tournament");
            }
        }else{
            if (ow.getData().equals("ok")) {
                JOptionPane.showMessageDialog(this, "Join successfully");
                Tournament t = listTournaments.get(idTIndex - 1);
                
                removePlayInTournament();
                
                PlayInTournamentFrm pitf = new PlayInTournamentFrm(myAccount, myControl, t);
                pitf.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error when joining to the tournament");
            }
        }
        
    }
    
    public void removePlayInTournament(){
        ObjectWrapper fun = null;
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if (activeFun.getData() instanceof PlayInTournamentFrm) {
                fun = activeFun;

            }
        }
        if (fun != null)
            myControl.getMyFuntion().remove(fun);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTournament = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Join Tournament");

        tblTournament.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTournament.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTournamentMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTournament);

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(158, 158, 158)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(190, 190, 190)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblTournamentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTournamentMouseClicked
        boolean joined = false;
        int selected = tblTournament.getSelectedRow();
        if (selected >= 0) {
            idTIndex = (int) model.getValueAt(selected, 0);
            Tournament t = listTournaments.get(idTIndex - 1);
            for (TournamentUser tu : t.getListTournamentUsers()) {
                if (tu.getUser().getId() == myAccount.getId()) {
                    joined = true;
                    break;
                }
            }
            if (joined) {
                removePlayInTournament();
                
                PlayInTournamentFrm pitf = new PlayInTournamentFrm(myAccount, myControl, t);
                pitf.setVisible(true);
                this.dispose();
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure to join this tournament?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // donng goi du lieu de gui di
                    TournamentUser tu = new TournamentUser();
                    tu.setUser(myAccount);
                    
                    Tournament tournamentTemp = t;
                    tournamentTemp.getListTournamentUsers().add(tu);
                    
                    t.getListTournamentUsers().set(0, tu);
                    
                    listTournaments.set(idTIndex - 1, tournamentTemp);
                    fillTable();
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.JOIN_TO_THE_TOURNAMENT, t));

                    
                }
            }

        }

    }//GEN-LAST:event_tblTournamentMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((HomeFrm)myControl.getForm()).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ((HomeFrm)myControl.getForm()).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tblTournament;
    // End of variables declaration//GEN-END:variables
}
