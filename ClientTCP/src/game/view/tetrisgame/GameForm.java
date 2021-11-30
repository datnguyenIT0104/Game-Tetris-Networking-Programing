package game.view.tetrisgame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import model.Match;
import model.Mode;
import model.ObjectWrapper;
import model.Result;
import model.User;
import tcp.client.control.ClientCtr;
import tcp.client.view.general.HomeFrm;
import tcp.client.view.group.PlayInGroupFrm;
import tcp.client.view.tournament.PlayInTournamentFrm;

/**
 *
 * @author DatIT
 */
public class GameForm extends JFrame {

    private GameArea ga;
    private BlockArea ba;
    private ClientCtr myControl;
    private Match myMatch;
    private User myAccount;
    private GameThread gameThread;
    private int score;
    private long timeAlive;

    public GameForm(ClientCtr myControl, Match myMatch, User myAccount) {
        initComponents();
        this.myControl = myControl;
        this.myMatch = myMatch;
        this.myAccount = myAccount;
//        this.result = result;

        ga = new GameArea(jpnGameAreaPlaceholder, 10);
        ba = new BlockArea(palBlockArea, 8);
        this.add(ba);
        this.add(ga);
        initUsername();

        // them mang random
        ga.setRandom(myMatch.getRandomBlock());
        ba.setRandom(myMatch.getRandomBlock());
        startGame();

        initControls();

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.SEND_INFO_TO_CLIENT, this));

    }
//
//    private GameForm() {// test
//        initComponents();
//        ga = new GameArea( jpnGameAreaPlaceholder, 10);
//        ba = new BlockArea(palBlockArea, 8);
//        this.add(ba);
//        this.add(ga);
//        
//    }

    private void initUsername() {
        labUsernamePlayer.setText(myAccount.getUsername());
    }

    private void initControls() {
        InputMap im = this.getRootPane().getInputMap();
        ActionMap am = this.getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");

        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.moveBlockRight();
            }
        });

        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.moveBlockLeft();
            }
        });

        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.rotateBlock();
            }
        });

        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.dropBlock();
            }
        });

    }

    public void startGame() {
        ga.initBackgroundArray();
        gameThread = new GameThread(ga, this, myMatch.getMode(), ba);
        gameThread.start();

    }

    public void gameOver(boolean gameOver, boolean outGame) {
        for (int i = 0; i < myMatch.getListResult().size(); i++) {
            Result r = myMatch.getListResult().get(i);
            if (r.getUser().getId() == myAccount.getId()) {

                //                myMatch.getListResult().get(i).setOutcome(0);
                myMatch.getListResult().get(i).setScore(score);
                myMatch.getListResult().get(i).setTimeAlive(timeAlive);

                if (gameOver) {
                    myMatch.getListResult().get(i).setOutcome(Result.LOSE);

                }
                myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND_RESULT_TO_SERVER, myMatch));

                if (outGame) {
                    System.exit(0);
                }
                // quay lai giai dau neu troi trong giai dau

                if (myMatch.getTournament() != null) {
                    returnTournament();
                }
//                else if (myMatch.getGroup() != null) {
//                    returnGroup();
//                } else {
//                    ((HomeFrm) myControl.getForm()).setVisible(true);
//                }

                this.dispose();
                break;

            }
        }
    }

    private void returnTournament() {

        ObjectWrapper fun = null;
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if (activeFun.getData() instanceof PlayInTournamentFrm) {
                fun = activeFun;

            }
        }
        if (fun != null) {
            myControl.getMyFuntion().remove(fun);
        }
        PlayInTournamentFrm pitf = new PlayInTournamentFrm(myAccount, myControl, myMatch.getTournament());
        pitf.setVisible(true);
        this.dispose();
    }


    public void updateScore(int score, long timeAlive) {
        this.score = score;
        this.timeAlive = timeAlive;
        txtScore.setText("Score: " + score);
    }

    public void updateLevel(int level) {
        txtLevel.setText("Level: " + level);
    }

    public void updateTimer(String time, long timeRemain) {// cap nhat thoi gian
        if (timeRemain <= 0) {
            txtTimer.setForeground(Color.RED);
        }

        txtTimer.setText(time);
    }

    public int[] getRandomList() {
        return ga.getRandom();
    }

    public void setRandomList(int[] randomL) {
        ga.setRandom(randomL);
    }

    public void receiveDataProcessing(ObjectWrapper ow) {
        try {
            System.out.println("nhan data");
            if (ow.getPerformative() == ObjectWrapper.SEND_INFO_TO_CLIENT) {

                Match match2 = (Match) ow.getData();
                for (int i = 0; i < match2.getListResult().size(); i++) {
                    Result r = match2.getListResult().get(i);
                    if (r.getUser().getId() == myAccount.getId()) {

                        Result enemy = match2.getListResult().get((i + 1) % 2);// lay ket qua cua doi thu
                        if (enemy.getOutcome() != -1) {
                            if (enemy.getOutcome() == Result.LOSE) {
                                match2.getListResult().get(i).setOutcome(Result.WIN);
                            } else {
                                match2.getListResult().get(i).setOutcome(Result.LOSE);
                            }
                        }

                        match2.getListResult().get(i).setScore(score);
                        match2.getListResult().get(i).setTimeAlive(timeAlive);
                        ga.gameFinish();
//                        gameThread.stop();
                        gameThread.interrupt();

                        myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND2_RESULT_TO_SERVER, match2));

                        // quay lai giai dau
                        if (myMatch.getTournament() != null) {
                            returnTournament();
                        }
                        this.dispose();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void main(String args[]) {
//        
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new GameForm().setVisible(true);
//            }
//        });
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnGameAreaPlaceholder = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        labUsernamePlayer = new javax.swing.JLabel();
        txtScore = new javax.swing.JLabel();
        txtLevel = new javax.swing.JLabel();
        txtTimer = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        palBlockArea = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpnGameAreaPlaceholder.setBackground(new java.awt.Color(238, 238, 238));
        jpnGameAreaPlaceholder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnGameAreaPlaceholderLayout = new javax.swing.GroupLayout(jpnGameAreaPlaceholder);
        jpnGameAreaPlaceholder.setLayout(jpnGameAreaPlaceholderLayout);
        jpnGameAreaPlaceholderLayout.setHorizontalGroup(
            jpnGameAreaPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );
        jpnGameAreaPlaceholderLayout.setVerticalGroup(
            jpnGameAreaPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 358, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("GAME TETRIS");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Player:");

        labUsernamePlayer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labUsernamePlayer.setText("player");

        txtScore.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtScore.setText("Score: 0");

        txtLevel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtLevel.setText("Level: 1");

        txtTimer.setEditable(false);
        txtTimer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTimer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTimer.setText("00:00");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Timer:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtScore)
                    .addComponent(txtLevel)
                    .addComponent(jLabel2)
                    .addComponent(labUsernamePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labUsernamePlayer)
                .addGap(18, 18, 18)
                .addComponent(txtScore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtLevel)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(19, 19, 19))
        );

        palBlockArea.setBackground(new java.awt.Color(238, 238, 238));

        javax.swing.GroupLayout palBlockAreaLayout = new javax.swing.GroupLayout(palBlockArea);
        palBlockArea.setLayout(palBlockAreaLayout);
        palBlockAreaLayout.setHorizontalGroup(
            palBlockAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        palBlockAreaLayout.setVerticalGroup(
            palBlockAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("NEXT BLOCK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(jLabel1)
                .addContainerGap(183, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpnGameAreaPlaceholder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(palBlockArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(64, 64, 64))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(palBlockArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpnGameAreaPlaceholder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        gameOver(true, true);
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jpnGameAreaPlaceholder;
    private javax.swing.JLabel labUsernamePlayer;
    private javax.swing.JPanel palBlockArea;
    private javax.swing.JLabel txtLevel;
    private javax.swing.JLabel txtScore;
    private javax.swing.JTextField txtTimer;
    // End of variables declaration//GEN-END:variables
}
