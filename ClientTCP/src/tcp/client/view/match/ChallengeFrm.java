package tcp.client.view.match;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Group;
import model.Match;
import model.Mode;
import model.ObjectWrapper;
import model.Result;
import model.Tournament;
import model.User;
import tcp.client.control.ClientCtr;
import tcp.client.view.general.HomeFrm;

/**
 *
 * @author DatIT
 */
public class ChallengeFrm extends javax.swing.JFrame {

    private ClientCtr myControl;
    private ArrayList<Mode> listModes;
    private DefaultTableModel model;
    private User receiveUser;
    private User myAccount;
    private Tournament tournament;
    private Group group;
    // choi bt
    public ChallengeFrm(ClientCtr myControl, User sendUser, User myAccount) {
        initComponents();
        this.myControl = myControl;
        this.receiveUser = sendUser;
        this.myAccount = myAccount;
        this.tournament = null;
        this.group = null;
        
        
        this.pack();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_MODE, this));
        initTable();
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MODE, "GetMode"));
    }
    // choi trong giai dau
    public ChallengeFrm(ClientCtr myControl, User sendUser, User myAccount, Tournament tournament) {
        initComponents();
        this.myControl = myControl;
        this.receiveUser = sendUser;
        this.myAccount = myAccount;
        this.tournament = tournament;
        
        this.pack();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_MODE, this));
        initTable();
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MODE, "GetMode"));
    }
    // choi trong nhom
    public ChallengeFrm(ClientCtr myControl, User sendUser, User myAccount, Group group) {
        initComponents();
        this.myControl = myControl;
        this.receiveUser = sendUser;
        this.myAccount = myAccount;
        this.group = group;
        
        this.pack();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_MODE, this));
        initTable();
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_MODE, "GetMode"));
    }
    private void initTable(){
        listModes = new ArrayList<>();
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        model.setColumnIdentifiers(new Object[]{
            "ID", "NAME", "TIME(MINUTE)", "SPEED"
        });
        tblMode.setModel(model);
        
    }
    private void fillTable(){
        model.setRowCount(0);
        for (Mode mode : listModes) {
            model.addRow(new Object[]{
                mode.getId(),mode.getName(), mode.getTime(), mode.getSpeed()
            });
        }
        model.fireTableDataChanged();
    }
    public void receiveDataProcessing(ObjectWrapper ow){
        if( ow.getPerformative() == ObjectWrapper.REPLY_GET_MODE){
            listModes = (ArrayList<Mode>) ow.getData();
            fillTable();
//            System.out.println("Size of list: " + listModes.size());
        }
        
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaMessage = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMode = new javax.swing.JTable();
        btnSend = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Challenger");

        jLabel2.setText("Message:");

        txaMessage.setColumns(20);
        txaMessage.setRows(5);
        jScrollPane1.setViewportView(txaMessage);

        jLabel3.setText("Mode:");

        tblMode.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tblMode);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSend)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        Match match = new Match();
        match.setNote(txaMessage.getText().trim());
        
        Mode mode = new Mode();
        int selected = tblMode.getSelectedRow();
        if( selected >= 0){
            int id = (int) model.getValueAt(selected, 0);
            for (Mode m : listModes) {
                if( m.getId() == id){
                    mode = m;
                    break;
                }
            }
            match.setMode(mode);
            Result result = new Result();// them nguoi nhan
            result.setUser(receiveUser);
            
            Result result1 = new Result();// them nguoi gui
            result1.setUser(myAccount);
            
            match.getListResult().add(result);
            match.getListResult().add(result1);
            
            Date now = new Date();
            match.setPlayTime(now);
            
            // them giai dau
            match.setTournament(tournament);
            // them nhom
            match.setGroup(group);
            
            myControl.sendData(new ObjectWrapper(ObjectWrapper.CHALLENGE_COMMUNICATE, match));
            this.dispose();
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        ((HomeFrm)myControl.getForm()).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tblMode;
    private javax.swing.JTextArea txaMessage;
    // End of variables declaration//GEN-END:variables
}
