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
import model.game.GameAreaEncode;
import model.game.GameThreadEncode;
import model.game.TetrisBlockEncode;

/**
 *
 * @author DatIT
 */
public class GameForm extends JFrame {

    protected GameArea ga;
    protected BlockArea ba;
//    private ClientCtr myControl;
    protected Match myMatch;
    protected User myAccount;
    protected GameThread gameThread;
    protected int score;
    protected long timeAlive;
    /*
    Những phần cần chuyển sang server
    1. Backgroud: khối đứng yên
    2. totalTime: thời gian chơi còn lại
    3. distance: khoảng thời gian đã chơi
    4. score: điểm
    5. Level: cấp độ hiện tại
    6. pause: thơi gian delay 1 khối khi rơi xuống
    7. index: thứ tự của khối đang rơi xuống
    8. tất cả các thuộc tính trong tetrisblock
    
    
    
    */
    public GameForm( Match myMatch, User myAccount) {
        initComponents();
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

    }


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
        
    }
    public void setPropertiesThreadGame(){
        GameThreadEncode gte = null;
        for (Result result : myMatch.getListResult()) {
            if( result.getUser().getId() == myAccount.getId()){
                gte = result.getGte();
                break;
            }
        }
        if( gte != null){
            gameThread.restoreGame(gte, myMatch.getMode());
            
        }
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

    public void encode(){
        gameThread.interrupt();
        gameThread.stopTimer();
        
        TetrisBlock tetrisBlock = ga.getBlock();
        TetrisBlockEncode tbe = new TetrisBlockEncode();
        tetrisBlock.encode(tbe);
        // gamearea
        GameAreaEncode gae = new GameAreaEncode();
        ga.encode(gae);
        gae.setBlock(tbe);
        // tetris thread
        GameThreadEncode gte = new GameThreadEncode();
        gameThread.encode(gte);
        gte.setGae(gae);
        for (int i = 0; i < myMatch.getListResult().size(); i++) {
            Result r = myMatch.getListResult().get(i);
            if (r.getUser().getId() == myAccount.getId()) {
                myMatch.getListResult().get(i).setGte(gte);

            }
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        palBound = new javax.swing.JPanel();
        jpnGameAreaPlaceholder = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        palBlockArea = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        labUsernamePlayer = new javax.swing.JLabel();
        txtScore = new javax.swing.JLabel();
        txtLevel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTimer = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        palBound.setLayout(null);

        jpnGameAreaPlaceholder.setBackground(new java.awt.Color(15, 25, 35));
        jpnGameAreaPlaceholder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpnGameAreaPlaceholderLayout = new javax.swing.GroupLayout(jpnGameAreaPlaceholder);
        jpnGameAreaPlaceholder.setLayout(jpnGameAreaPlaceholderLayout);
        jpnGameAreaPlaceholderLayout.setHorizontalGroup(
            jpnGameAreaPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );
        jpnGameAreaPlaceholderLayout.setVerticalGroup(
            jpnGameAreaPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );

        palBound.add(jpnGameAreaPlaceholder);
        jpnGameAreaPlaceholder.setBounds(20, 100, 200, 400);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GAME TETRIS");
        palBound.add(jLabel1);
        jLabel1.setBounds(10, 11, 420, 29);

        palBlockArea.setBackground(new java.awt.Color(15, 25, 35));

        javax.swing.GroupLayout palBlockAreaLayout = new javax.swing.GroupLayout(palBlockArea);
        palBlockArea.setLayout(palBlockAreaLayout);
        palBlockAreaLayout.setHorizontalGroup(
            palBlockAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 165, Short.MAX_VALUE)
        );
        palBlockAreaLayout.setVerticalGroup(
            palBlockAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        palBound.add(palBlockArea);
        palBlockArea.setBounds(250, 160, 165, 98);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NEXT BLOCK");
        palBound.add(jLabel3);
        jLabel3.setBounds(290, 130, 92, 17);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Player:");
        palBound.add(jLabel2);
        jLabel2.setBounds(270, 300, 53, 22);

        labUsernamePlayer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labUsernamePlayer.setForeground(new java.awt.Color(255, 255, 255));
        labUsernamePlayer.setText("player");
        palBound.add(labUsernamePlayer);
        labUsernamePlayer.setBounds(270, 330, 141, 22);

        txtScore.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtScore.setForeground(new java.awt.Color(255, 255, 255));
        txtScore.setText("Score: 0");
        palBound.add(txtScore);
        txtScore.setBounds(270, 370, 100, 22);

        txtLevel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtLevel.setForeground(new java.awt.Color(255, 255, 255));
        txtLevel.setText("Level: 1");
        palBound.add(txtLevel);
        txtLevel.setBounds(270, 410, 90, 22);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Timer:");
        palBound.add(jLabel4);
        jLabel4.setBounds(270, 450, 51, 22);

        txtTimer.setEditable(false);
        txtTimer.setBackground(new java.awt.Color(15, 25, 35));
        txtTimer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTimer.setForeground(new java.awt.Color(255, 255, 255));
        txtTimer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTimer.setText("00:00");
        palBound.add(txtTimer);
        txtTimer.setBounds(330, 450, 90, 34);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/backGround.png"))); // NOI18N
        palBound.add(jLabel6);
        jLabel6.setBounds(0, -6, 450, 560);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(palBound, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(palBound, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jpnGameAreaPlaceholder;
    private javax.swing.JLabel labUsernamePlayer;
    private javax.swing.JPanel palBlockArea;
    private javax.swing.JPanel palBound;
    private javax.swing.JLabel txtLevel;
    private javax.swing.JLabel txtScore;
    private javax.swing.JTextField txtTimer;
    // End of variables declaration//GEN-END:variables
}
