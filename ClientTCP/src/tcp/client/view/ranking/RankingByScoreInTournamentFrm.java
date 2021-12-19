package tcp.client.view.ranking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.ObjectWrapper;
import model.Tournament;
import model.TournamentUser;
import tcp.client.control.ClientCtr;

/**
 *
 * @author DatIT
 */
public class RankingByScoreInTournamentFrm extends javax.swing.JFrame {

    private Tournament myTournament;
    private ClientCtr myControl;
    private DefaultTableModel model;
    private ArrayList<TournamentUser> listPlayer;

    public RankingByScoreInTournamentFrm(Tournament myTournament, ClientCtr myControl) {
        initComponents();
        this.myTournament = myTournament;
        this.myControl = myControl;
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_RANKING_BY_SCORE_IN_TOURNAMEN, this));
        initTable();
    }
    private void initTable(){
        model = (DefaultTableModel) tblTournamentUser.getModel();
        tblTournamentUser.setModel(model);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtTournamentName.setText(myTournament.getName());
        labEndTime.setText("End time: " + sdf.format(myTournament.getEndDate()));
        
        myControl.sendData(new ObjectWrapper(ObjectWrapper.RANKING_BY_SCORE_IN_TOURNAMENT, myTournament));
        
    }
    public void fillTable(){
        
        model.setRowCount(0);
        for (TournamentUser tu : listPlayer) {
            model.addRow(new Object[]{
                tu.getUser().getId(), tu.getUser().getUsername(), tu.getUser().getName(), tu.getTotalScore()
            });
        }
        
        model.fireTableDataChanged();
    }
    public void receiveDataProcessing(ObjectWrapper ow){
        if( ow.getPerformative() == ObjectWrapper.REPLY_RANKING_BY_SCORE_IN_TOURNAMEN){
            listPlayer = (ArrayList<TournamentUser>) ow.getData();
            
            fillTable();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTournamentName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        labEndTime = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTournamentUser = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        txtTournamentName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtTournamentName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTournamentName.setText("Tournament Name");

        labEndTime.setText("End time:");

        tblTournamentUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "USERNAME", "NAME", "TOTAL SCORE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTournamentUser.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                    .addComponent(txtTournamentName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTournamentName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labEndTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblTournamentUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTournamentUserMouseClicked
        
    }//GEN-LAST:event_tblTournamentUserMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ObjectWrapper fun = null;
        for (ObjectWrapper ow : myControl.getMyFuntion()) {
            if( ow.getData() instanceof SelectTournamentFrm){
                fun = ow;
                break;
            }
        }
        if( fun  != null)
            myControl.getMyFuntion().remove(fun);
        SelectTournamentFrm frm = new SelectTournamentFrm(myControl);
        frm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labEndTime;
    private javax.swing.JTable tblTournamentUser;
    private javax.swing.JLabel txtTournamentName;
    // End of variables declaration//GEN-END:variables
}
