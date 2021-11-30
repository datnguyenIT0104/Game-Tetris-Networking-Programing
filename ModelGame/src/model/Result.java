package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class Result implements Serializable{
    private static final long serialVersionUID = 20210811019L;
    public static final int LOSE = 0;
    public static final int WIN = 1;
//    public static final int PLAYING = 2;
//    public static final int FINISH = 3;
    
    private int id;
    private int score;
    private int outcome;
    private User user;
    private long timeAlive;
    
    public Result() {
        outcome = -1;
        
    }

    public Result(int id, int score, int outcome, User user, long timeAlive) {
        this.id = id;
        this.score = score;
        this.outcome = outcome;
        this.user = user;
        this.timeAlive = timeAlive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(long timeAlive) {
        this.timeAlive = timeAlive;
    }
}
