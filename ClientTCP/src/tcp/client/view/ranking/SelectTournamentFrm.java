package tcp.client.view.ranking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.ObjectWrapper;
import model.Tournament;
import tcp.client.control.ClientCtr;
import tcp.client.view.general.HomeFrm;

/**
 *
 * @author DatIT
 */
public class SelectTournamentFrm extends javax.swing.JFrame {

    private ClientCtr myControl;
    private ArrayList<Tournament> listTournaments;
    private DefaultTableModel model;

    public SelectTournamentFrm(ClientCtr myControl) {
        initComponents();
        this.myControl = myControl;

        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_ALL_TOURNAMENT, this));
        initTable();
    }

    private void initTable() {
        model = (DefaultTableModel) tblTournament.getModel();
        tblTournament.setModel(model);
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_ALL_TOURNAMENT, "GetAllTournament"));

    }

    public void fillTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        model.setRowCount(0);
        for (Tournament tournament : listTournaments) {
            model.addRow(new Object[]{
                tournament.getId(), tournament.getName(), sdf.format(tournament.getEndDate()), tournament.getListTournamentUsers().size()
            });
        }
        model.fireTableDataChanged();
    }

    public void receiveDataProcessing(ObjectWrapper ow) {
        if (ow.getData() instanceof ArrayList<?>) {
            listTournaments = (ArrayList<Tournament>) ow.getData();
            fillTable();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTournament = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("List Tournament");

        tblTournament.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "END DATE", "NUMBER OF PLAYER"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTournament.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(187, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(185, 185, 185))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBack)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((HomeFrm) myControl.getForm()).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void tblTournamentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTournamentMouseClicked
        int selected = tblTournament.getSelectedRow();
        if (selected >= 0) {
            int idT = (int) model.getValueAt(selected, 0);

            for (Tournament tournament : listTournaments) {
                if (tournament.getId() == idT) {
                    ObjectWrapper fun = null;
                    for (ObjectWrapper ow : myControl.getMyFuntion()) {
                        if (ow.getData() instanceof RankingByScoreInTournamentFrm) {
                            fun = ow;
                            break;
                        }
                    }
                    if (fun != null) {
                        myControl.getMyFuntion().remove(fun);
                    }
                    RankingByScoreInTournamentFrm rbsitf = new RankingByScoreInTournamentFrm(tournament, myControl);
                    rbsitf.setVisible(true);

                    this.dispose();
                    return;
                }
            }
        }
    }//GEN-LAST:event_tblTournamentMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblTournament;
    // End of variables declaration//GEN-END:variables
}
