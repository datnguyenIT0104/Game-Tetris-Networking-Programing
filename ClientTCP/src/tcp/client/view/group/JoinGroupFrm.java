package tcp.client.view.group;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Group;
import model.Joining;
import model.ObjectWrapper;
import model.User;
import tcp.client.control.ClientCtr;

/**
 *
 * @author DatIT
 */
public class JoinGroupFrm extends JFrame {
    private ClientCtr myControl;
    private User myAccount;
    private DefaultTableModel model;
    private ArrayList<Group> listGroups;
    private int selected;
    
    public JoinGroupFrm(ClientCtr myControl, User myAccount) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;
        
        listGroups = new ArrayList<>();
        initTable();
                 
        
                
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_JOIN_GROUP, this));
        myControl.sendData(new ObjectWrapper(ObjectWrapper.JOIN_GROUP, myAccount));
    }
    
    private void initTable(){
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new Object[]{
            "INDEX", "ID", "NAME","NUMBER OF MEMBERS"
        });
        
        tblGroup.setModel(model);
    }
    public void receiveDataProcessing(ObjectWrapper ow){
        if( ow.getData() instanceof ArrayList<?>){
            listGroups = (ArrayList<Group>) ow.getData();
            // khoi tao bang
            fillTable();
        }else{
            if( ow.getData() instanceof Group){
                deleteGroup();
                JOptionPane.showMessageDialog(this, "Join Successfully");
                
                myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_GROUP_TO_ALL_CLIENT, "UpdateToAll"));
                // update thanh vien neu dang o trong nhom
                myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_MEMBER_OF_GROUP, ow.getData()));
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Fail when join group", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void fillTable(){
        model.setRowCount(0);
        int index = 1;
        for (Group g : listGroups) {
            model.addRow(new Object[]{
                index++, g.getId(), g.getName(), g.getNumMember()
            });
        }
        model.fireTableDataChanged();
        
    }
    private void deleteGroup(){
//        selected = tblGroup.getSelectedRow();
        if( selected >= 0){
            int idG = (int) model.getValueAt(selected, 1);
            for (Group g : listGroups) {
                if( g.getId() == idG){
                    listGroups.remove(g);
                    fillTable();
                    break;
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGroup = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Join Group");

        tblGroup.setModel(new javax.swing.table.DefaultTableModel(
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
        tblGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroupMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGroup);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(jLabel1)
                        .addGap(0, 179, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSeparator1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblGroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupMouseClicked
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure to join this group?", "Confirm", JOptionPane.YES_NO_OPTION);
        if( choice == JOptionPane.NO_OPTION) return;
        if(choice == JOptionPane.YES_OPTION){
            selected = tblGroup.getSelectedRow();
        
            int idG = (int) model.getValueAt(selected, 1);
            for (Group g : listGroups) {
                if( g.getId() == idG){
                    Joining j = new Joining();
                    j.setRoleInGroup("member");
                    j.setUser(myAccount);
                    g.getListJoining().add(j);
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.JOIN_GROUP, g));
                    break;
                }
            }
            
        }
        
    }//GEN-LAST:event_tblGroupMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblGroup;
    // End of variables declaration//GEN-END:variables
}
