
package game.view.tetrisgame;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Match;
import model.Mode;
import model.ObjectWrapper;
import model.Result;
import model.User;
import model.game.GameThreadEncode;

/**
 *
 * @author DatIT
 */
public class GameThread extends Thread{
    private GameArea gameArea;
    private BlockArea blockArea;
    private GameForm gf;
    
    private Mode mode;
    private long totalTime;
    private TimerThread timerThread;
    
    private long distance;
    private long distanceTemp;
    
    private int score;
    
    private int level = 1;
    private int scorePerLevel = 40;
    
    private int pause = 1000;
    private int speedUpPerLevel = 100;

    private int index;
    
    public GameThread(GameArea gameArea, GameForm gf, Mode mode, BlockArea blockArea) {
        this.gameArea = gameArea;
        this.gf = gf;
        this.mode = mode;
        this.blockArea = blockArea;
        
        score = 0;
        distance = 0;
        totalTime = 1;
        index = -1;
        if( !mode.getName().equals("Auto")){
            level = (pause - mode.getSpeed())/100 + 1;
            pause = mode.getSpeed();
            totalTime = TimeUnit.MINUTES.toMillis(mode.getTime());
            timerThread = new TimerThread();
            timerThread.start();
            
        }
        gf.updateScore(score, distance);
        gf.updateLevel(level);
    }
    
    
    @Override
    public void run(){
        try {
            long mili1 = System.currentTimeMillis();
            long mili2;
            if( index == -1) index = 0;
            int rotation = blockArea.spawnBlock(index+1);
            distanceTemp = 0;
            boolean gameOver = false;                       // xem co cham canh tren k
            while(true){
                index = (++index)%10000;
                gameArea.spawnBlock(index, rotation);
                rotation = blockArea.spawnBlock(index + 1 );
                while( gameArea.moveBlockDown()){
                    if( totalTime <= 0) break;              // neu nhu het tg thi k choi nua
                    try {
                        Thread.sleep(pause);
                    } catch (InterruptedException ex) {
                        return;
                    }
                    mili2 = System.currentTimeMillis();
                    distanceTemp = mili2 - mili1;
                    gf.updateScore(score, distance + distanceTemp);
                }
                if( totalTime <= 0) break;              // neu nhu het tg thi k choi nua
                
                if( gameArea.isBlockOutOfBound()){
                    gameOver = true;
                    gf.gameOver(true, false);
                    gameArea.repaint();
                    break;
                }
                 // chuyen khoi thanh background va tinh diem
                gameArea.moveBlockToBackground();
                
                int clearL = gameArea.clearLines();
                if( clearL == 1){
                    score += clearL*10;
                }else if( clearL >= 2){
                    score += clearL*10 + 3;      // cong them diem bonuos
                }else if( clearL >= 4){
                    score += clearL*10 + 5;      // cong them diem bonuos
                }
                
//                score += gameArea.clearLines()*3;
                gf.updateScore(score, distance);

                if( mode.getName().equals("Auto")){
                    int lvl = score / scorePerLevel + 1;
                    if (lvl > level) {
                        level = lvl;
                        gf.updateLevel(level);
                        pause -= speedUpPerLevel;
                    }
                }
                mili2 = System.currentTimeMillis();
                distanceTemp = mili2 - mili1;
                gf.updateScore(score, distance + distanceTemp);

            }
            mili2 = System.currentTimeMillis();
            distanceTemp = mili2 - mili1;
            gf.updateScore(score, distance + distanceTemp);
            
            timerThread.stop();
            timerThread = null;
            Thread.sleep(100);
            if(!gameOver){
                gf.gameOver(gameOver, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreGame(GameThreadEncode gte, Mode mode){
        // lay lai thuoc tinh cua gamethread
        this.mode = mode;
        totalTime = gte.getTotalTime();
        distance = gte.getDistance();
        score = gte.getScore();
        level = gte.getLevel();
        pause = gte.getPause();
        index = gte.getIndex();
        // gamearea
        gameArea.spawnBlock(index, 1);
        gameArea.restoreGame(gte.getGae());
        
        // cap nhat thong tin
        
        gf.updateScore(score, distance + distanceTemp);
        gf.updateLevel(level);
    }
    public void encode(GameThreadEncode gte){
        gte.setTotalTime(totalTime);
        gte.setDistance(distance + distanceTemp);
        gte.setScore(score);
        gte.setLevel(level);
        gte.setPause(pause);
        gte.setIndex(index);
    }
    public long getDistance() {
        return distance;
    }

    public int getScore() {
        return score;
    }
    public void stopTimer(){
        timerThread.interrupt();
    }
    class TimerThread extends Thread{
        @Override
        public void run(){
            while(true){
                try {
                    /*
                    1 phut = 60s
                    1s = 1000 millis
                    1 phut = 60000
                    */
                    
                    String time = "" + ( totalTime/60000) +":" + ( totalTime%60000)/1000;
                    
                    
                    gf.updateTimer(time, totalTime/60000);
                    
                    sleep(1000);
                    totalTime -= 1000;
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }
}
