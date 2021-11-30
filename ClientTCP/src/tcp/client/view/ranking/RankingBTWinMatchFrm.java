package tcp.client.view.ranking;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import model.ObjectWrapper;
import model.Ranking;
import model.User;
import tcp.client.control.ClientCtr;

/**
 *
 * @author DatIT
 */
public class RankingBTWinMatchFrm extends JFrame {

    private User myAccount;
    private ClientCtr myControl;
    private ArrayList<Ranking> list;
    private DefaultTableModel model;
    
    public RankingBTWinMatchFrm(User myAccount, ClientCtr myControl) {
        initComponents();
        this.myAccount = myAccount;
        this.myControl = myControl;
        
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_RANKING_BY_TOTAL_WIN_MATCH, this));
        initTable();
    }
    
    public void initTable(){
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
                
            }
        };
        myControl.sendData(new ObjectWrapper(ObjectWrapper.RANKING_BY_TOTAL_WIN_MATCH, myAccount));
    }
    
    public void receiveDataProcessing(ObjectWrapper ow){
        
        if (ow.getPerformative() == ObjectWrapper.REPLY_RANKING_BY_TOTAL_WIN_MATCH) {
            model.setRowCount(0);
            list = (ArrayList<Ranking>) ow.getData();
            model.setColumnIdentifiers(new Object[]{
                "INDEX", "USERNAME", "NAME", "ROLE", "TOTAL MATCH"
            });
            int index = 1;

            for (Ranking ranking : list) {
                model.addRow(new Object[]{
                    index++, ranking.getUsername(), ranking.getName(), ranking.getRole(), ranking.getTotalWinMatch()
                });
            }
            tblRanking.setModel(model);
        }
//        }else if( performative == ObjectWrapper.RANKING_BY_SCORE_IN_TOURNAMENT){
//            myControl.sendData(new ObjectWrapper(ObjectWrapper.RANKING_BY_SCORE_IN_TOURNAMENT,"RANKING"));
//            
//            data = myControl.receiveData();
//            if( data.getPerformative() == ObjectWrapper.REPLY_RANKING_BY_SCORE_IN_TOURNAMEN){
//                list = (ArrayList<Ranking>) data.getData();
//                model.setColumnIdentifiers(new Object[]{
//                    "INDEX", "USERNAME", "NAME", "ROLE", "TOTAL MATCH"
//                });
//                int index = 1;
//                
//                for (Ranking ranking : list) {
//                    model.addRow(new Object[]{
//                        index++, ranking.getUsername(), ranking.getName(), ranking.getRole(), ranking.getTotalWinMatch()
//                    });
//                }
//                tblRanking.setModel(model);
//            }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRanking = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Ranking");

        tblRanking.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblRanking);

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
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack)
                .addGap(9, 9, 9))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tblRanking;
    // End of variables declaration//GEN-END:variables
}
