package tcp.client.view.general;

import model.Friend;
import model.ObjectWrapper;
import model.User;
import tcp.client.control.ClientCtr;

/**
 *
 * @author DatIT
 */
public class ChatPrivateFrm extends javax.swing.JFrame {

    private User myAccount;
    private ClientCtr myControl;
    private Friend myFriend;
    private int index;
    
    public ChatPrivateFrm(User myAccount, ClientCtr myControl, Friend myFriend) {
        initComponents();
        this.myAccount = myAccount;
        this.myControl = myControl;
        this.myFriend = myFriend;
        
        initForm();
    }
    private void initForm(){
        // lay tin nhan cu
        String oldMess = "";
        index = 0;
        for (Friend item : myAccount.getListFriend()) {
            if( item.getId() == myFriend.getId()){
                oldMess = item.getMessage();
                
                break;
            }
            index++;
        }
        
        txaBoard.setText(oldMess);
        labNameFriend.setText("Chat with " + myFriend.getName());
//        System.out.println("ID Frind: "  + myFriend.getId());
//        System.out.println("index : " + index);
    }
    
    public void receiveMessage(){
        txaBoard.setText(txaBoard.getText() + myFriend.getMessage());
        
        // luu lai tin nhan
        myAccount.getListFriend().get(index).setMessage(txaBoard.getText());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaBoard = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        btnSend = new javax.swing.JButton();
        labNameFriend = new javax.swing.JLabel();
        txtMessage = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chat With Friend");

        txaBoard.setEditable(false);
        txaBoard.setColumns(20);
        txaBoard.setRows(5);
        jScrollPane1.setViewportView(txaBoard);

        btnSend.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        labNameFriend.setText("Friend:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSend))
                    .addComponent(labNameFriend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(labNameFriend)
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMessage)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String newText = " " + myAccount.getUsername() +": "+ txtMessage.getText() + "\n";
        // luu lai
        myAccount.getListFriend().get(index).setMessage( txaBoard.getText() + newText);

        // dong goi gui du lieu
        Friend friend = new Friend();
        friend.setId( myAccount.getId());
        friend.setUsername( myAccount.getUsername());
        friend.setName(myAccount.getName());
        friend.setMessage(newText);
        
        User userTemp = new User();
        userTemp.setId(myFriend.getId());
        userTemp.getListFriend().add(friend);
        
        myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND_MESSAGE_TO_FRIEND, userTemp));
        
        this.dispose();
    }//GEN-LAST:event_btnSendActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labNameFriend;
    private javax.swing.JTextArea txaBoard;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
