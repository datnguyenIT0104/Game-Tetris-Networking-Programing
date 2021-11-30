package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class TournamentUser implements Serializable{
    private static final long serialVersionUID = 20210811021L;
    private int id;
    private int totalScore;
    private User user;
    public TournamentUser() {
    }

    public TournamentUser(int id, int totalScore, User user) {
        this.id = id;
        this.totalScore = totalScore;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
