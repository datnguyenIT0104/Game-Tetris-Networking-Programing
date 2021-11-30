package tcp.client.view.group;

import java.util.ArrayList;
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
public class InvitationFrm extends javax.swing.JFrame {

    private ClientCtr myControl;
    private ArrayList<Group> listInvitation;
    private DefaultTableModel model;
    private User myAccount;

    public InvitationFrm(ClientCtr myControl, User myAccount) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_INVITATION_FROM_SERVER, this));
        initTable();

    }

    private void initTable() {
        model = (DefaultTableModel) tblInvitation.getModel();
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_INVITATION_FROM_SERVER, "GET_INVITATION"));

        tblInvitation.setModel(model);
    }

    public void fillTable() {
        model.setRowCount(0);
        for (Group group : listInvitation) {
            Joining joining = group.getListJoining().get(0);
            User send = joining.getUser();
            User receive = send.getListFriend().get(0);

            if (receive.getId() == myAccount.getId()) {
                model.addRow(new Object[]{
                    send.getName(), group.getName(), group.getNumMember()
                });
            }
        }

        model.fireTableDataChanged();
    }

    public void receiveDataProcessing(ObjectWrapper ow) {
        if (ow.getPerformative() == ObjectWrapper.REPLY_GET_INVITATION_FROM_SERVER) {
            listInvitation = (ArrayList<Group>) ow.getData();
            fillTable();

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvitation = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Invitation For You");

        tblInvitation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "WHO INVITED", "GROUP NAME", "MEMBER"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvitation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvitationMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInvitation);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblInvitationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvitationMouseClicked
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure to join this group?", "Confirm", JOptionPane.YES_NO_OPTION);
        int selected = tblInvitation.getSelectedRow();

        String nameGroup = (String) model.getValueAt(selected, 1);
        for (Group g : listInvitation) {
            if (g.getName().equals(nameGroup)) {
                Joining j = new Joining();
                j.setRoleInGroup("member");
                j.setUser(myAccount);

                g.getListJoining().clear();
                g.getListJoining().add(j);

                if (choice == JOptionPane.NO_OPTION) {
                    // khong dong y tham gia
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.DELETE_INVITATION, g));
                }
                if (choice == JOptionPane.YES_OPTION) {
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.JOIN_GROUP_BY_INVITATION, g));
                }
                this.dispose();
                break;
            }
        }


    }//GEN-LAST:event_tblInvitationMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblInvitation;
    // End of variables declaration//GEN-END:variables
}
