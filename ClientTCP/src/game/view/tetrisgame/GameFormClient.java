package game.view.tetrisgame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import model.Match;
import model.ObjectWrapper;
import model.Result;
import model.User;
import model.game.GameAreaEncode;
import model.game.GameThreadEncode;
import model.game.TetrisBlockEncode;
import tcp.client.control.ClientCtr;
import tcp.client.view.tournament.PlayInTournamentFrm;

/**
 *
 * @author DatIT
 */
public class GameFormClient extends GameForm {

    /*
    1. Thêm hàm tạo
    2. Thêm lớp điều khiển ClientCtr
    3. Thêm phương thức gameOver
    4. Thêm phương thức trở về giải đấu
    5. Thêm phương thức receiveDataProcessing
    6. Tạo phương thức xử lý out game
     */
    private ClientCtr myControl;

    public GameFormClient(Match myMatch, User myAccount) {
        super(myMatch, myAccount);
    }

    public GameFormClient(ClientCtr myControl, Match myMatch, User myAccount) {

        super(myMatch, myAccount);
        this.myControl = myControl;

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                formClosing();
//                gameOver(true, true);
            }
        });
        myControl.getMyFuntion().add(new ObjectWrapper(ObjectWrapper.SEND_INFO_TO_CLIENT, this));
    }

    @Override
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
                // am thanh ket thuc
                ga.gameFinish();
                // gui kq len server
                myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND_RESULT_TO_SERVER, myMatch));

                if (outGame) {
                    if(myControl != null)
                        myControl.closeConnnection();
                    System.exit(0);
                }
                // quay lai giai dau neu choi trong giai dau

//                if (myMatch.getTournament() != null) {
//                    returnTournament();
//                }
                
                this.dispose();
                break;

            }
        }
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
//                        if (myMatch.getTournament() != null) {
//                            returnTournament();
//                        }
                        this.dispose();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void backGame(){
        super.setPropertiesThreadGame();
    }
    public void formClosing() {
        super.encode();

        myControl.sendData(new ObjectWrapper(ObjectWrapper.SEND_INFOR_ABOUT_PLAYING_GAME, myMatch));
        // dong ket noi
        if(myControl != null)
            myControl.closeConnnection();
        System.exit(0);
    }

}
