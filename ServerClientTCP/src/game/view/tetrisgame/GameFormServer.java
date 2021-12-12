package game.view.tetrisgame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import model.Match;
import model.ObjectWrapper;
import model.Result;
import model.User;
import model.game.GameAreaEncode;
import model.game.GameThreadEncode;
import model.game.TetrisBlockEncode;
import tcp.server.control.TCPServerCtr;
import tcp.server.view.ServerTCPHomeFrm;

/**
 *
 * @author DatIT
 */
public class GameFormServer extends GameForm {

    private TCPServerCtr myControl;
    
    public GameFormServer(Match myMatch, User myAccount, TCPServerCtr myControl) {
        super(myMatch, myAccount);
        
        this.myControl = myControl;
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeGame();
//                gameOver(true, true);
            }
        });
        super.setPropertiesThreadGame();
        // an di gameformserver
        this.setVisible(false);
    }

    @Override
    public void gameOver(boolean gameOver, boolean outGame) {
        for (int i = 0; i < myMatch.getListResult().size(); i++) {
            Result r = myMatch.getListResult().get(i);
            if (r.getUser().getId() == myAccount.getId()) {
                if( gameOver)
                    myMatch.getListResult().get(i).setOutcome(Result.LOSE);
                
                myMatch.getListResult().get(i).setScore(score);
                myMatch.getListResult().get(i).setTimeAlive(timeAlive);
                
                User opponent = myMatch.getListResult().get( (i+1)%2).getUser();
                // kiem tra ng choi con lai co thoat game hay k?
                if( !checkGameFormAtServer())
                    myControl.publicSendCommunicate(new ObjectWrapper(ObjectWrapper.SEND_INFO_TO_CLIENT, myMatch), opponent);
                        
                this.setVisible(false);
                break;
            }
        }
    }

    public boolean checkGameFormAtServer(){
        ArrayList<GameForm> list = myControl.getListPlaying();
        User receiver = myMatch.getListResult().get(0).getUser();
        User sender = myMatch.getListResult().get(1).getUser();
        boolean isOut = false;
        ArrayList<GameForm> listRemove = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GameFormServer gfs = (GameFormServer)list.get(i);
            Match m = gfs.getMyMatch();
            User receiver1 = m.getListResult().get(0).getUser();
            User sender1 = m.getListResult().get(1).getUser();
            // kiem tra xem gameform cua doi thu co ton tai tren server hay k?
            if( receiver.getId() == receiver1.getId() && sender.getId() == sender1.getId() && 
                    myMatch.getPlayTime().equals(m.getPlayTime())){
                if( myAccount.getId() != gfs.getMyAccount().getId()){
                    
                    ((GameFormServer)list.get(i)).receiveDataFromServer(myMatch);
                    isOut = true;
                }
//                myControl.getListPlaying().remove(i);
                listRemove.add(gfs);
//                System.out.println("Size of listplay: " + myControl.getListPlaying().size());
            }
        }
        myControl.getListPlaying().removeAll(listRemove);
        // cap nhat
        myControl.getsHomeF().fillList( myControl.getListPlaying());
        return isOut;
    }
    
    public void receiveDataFromServer(Match match){
        for (int i = 0; i < match.getListResult().size(); i++) {
            Result r = match.getListResult().get(i);
            if (r.getUser().getId() == myAccount.getId()) {

                Result enemy = match.getListResult().get((i + 1) % 2);// lay ket qua cua doi thu
                if (enemy.getOutcome() != -1) {
                    if (enemy.getOutcome() == Result.LOSE) {
                        match.getListResult().get(i).setOutcome(Result.WIN);
                    } else {
                        match.getListResult().get(i).setOutcome(Result.LOSE);
                    }
                }

                match.getListResult().get(i).setScore(score);
                match.getListResult().get(i).setTimeAlive(timeAlive);
//                        gameThread.stop();
                gameThread.interrupt();

                // goi server de luu ket qua va cap nhat bang xep hang
                myControl.saveResultOf2GameForm(match);
                
                this.setVisible(false);
                break;
            }
        }
    }
            
    public void closeGame() {
        this.setVisible(false);

    }

    public Match receiveDataFromClient(Match match) {
        for (int i = 0; i < match.getListResult().size(); i++) {
            Result r = match.getListResult().get(i);
            if (r.getUser().getId() == myAccount.getId()) {

                Result enemy = match.getListResult().get((i + 1) % 2);// lay ket qua cua doi thu
                if (enemy.getOutcome() != -1) {
                    if (enemy.getOutcome() == Result.LOSE) {
                        match.getListResult().get(i).setOutcome(Result.WIN);
                    } else {
                        match.getListResult().get(i).setOutcome(Result.LOSE);
                    }
                }

                match.getListResult().get(i).setScore(score);
                match.getListResult().get(i).setTimeAlive(timeAlive);
//                        gameThread.stop();
                gameThread.interrupt();

                this.setVisible(false);
                break;
            }
        }
        return match;
    }

    public Match playerBack() {
        super.encode();
        this.setVisible(false);
        return myMatch;
    }

    public User getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(User myAccount) {
        this.myAccount = myAccount;
    }

    public Match getMyMatch() {
        return myMatch;
    }

    public void setMyMatch(Match myMatch) {
        this.myMatch = myMatch;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
