package model.game;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class GameThreadEncode implements Serializable{
    private GameAreaEncode gae;
    
    private long totalTime;
    private long distance;
    
    private int score;
    
    private int level = 1;
    private int scorePerLevel = 40;
    
    private int pause = 1000;
    private int speedUpPerLevel = 100;

    public GameThreadEncode() {
    }

    public GameThreadEncode(GameAreaEncode gae, long totalTime, long distance, int score) {
        this.gae = gae;
        this.totalTime = totalTime;
        this.distance = distance;
        this.score = score;
    }

    public GameAreaEncode getGae() {
        return gae;
    }

    public void setGae(GameAreaEncode gae) {
        this.gae = gae;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScorePerLevel() {
        return scorePerLevel;
    }

    public void setScorePerLevel(int scorePerLevel) {
        this.scorePerLevel = scorePerLevel;
    }

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public int getSpeedUpPerLevel() {
        return speedUpPerLevel;
    }

    public void setSpeedUpPerLevel(int speedUpPerLevel) {
        this.speedUpPerLevel = speedUpPerLevel;
    }
    
}
