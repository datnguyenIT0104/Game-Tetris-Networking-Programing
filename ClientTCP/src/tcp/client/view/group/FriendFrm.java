package tcp.client.view.group;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Friend;
import model.Group;
import model.Joining;
import model.ObjectWrapper;
import model.User;
import tcp.client.control.ClientCtr;

/**
 *
 * @author DatIT
 */
public class FriendFrm extends javax.swing.JFrame {

    private ClientCtr myControl;
    private User myAccount;
    private DefaultTableModel model;
    private Group group;
    private ArrayList<Joining> list;
    
    public FriendFrm(ClientCtr myControl, User myAccount, Group group, ArrayList<Joining> list) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;
        this.group = group;
        this.list = list;
        fillTable();
    }
    private void fillTable(){
        model = (DefaultTableModel) tblFriend.getModel();
        
        model.setRowCount(0);
        for (Friend friend : myAccount.getListFriend()) {
            boolean isJoined = false;
            for (Joining item : list) {
                if( item.getUser().getId() == friend.getId()){
                    isJoined = true;
                    break;
                }
            }
            if( isJoined) continue;
            
            model.addRow(new Object[]{
                friend.getId(), friend.getName(), friend.getRole()
            });
        }
        tblFriend.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFriend = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Invite Friend");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Invite Friend");

        tblFriend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "FRIEND NAME", "ROLE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFriendMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFriend);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFriendMouseClicked
        int selected = tblFriend.getSelectedRow();
        if( selected >= 0){
            int idF = (int) model.getValueAt(selected, 0);
            // dong goi du lieu
            
            
            Joining joining = new Joining();
            
            User send = new User();
            send.setId( myAccount.getId());
            send.setName(myAccount.getName());
            send.setUsername(myAccount.getUsername());
            
            for (Friend friend : myAccount.getListFriend()) {
                if( friend.getId() == idF){
                    send.getListFriend().add(friend);
                    
                    joining.setUser(send);
                    group.getListJoining().clear();
                    group.getListJoining().add(joining);
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND_INVITATION_GROUP, group));
                    
                    this.dispose();
                }
            }
        }
    }//GEN-LAST:event_tblFriendMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFriend;
    // End of variables declaration//GEN-END:variables
}
