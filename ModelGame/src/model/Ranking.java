package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class Ranking extends User implements Serializable{
    private static final long serialVersionUID = 20210811018L;
    private float winRate;
    private int totalWinMatch;
    private int scoreInTournament;
    private float winRateWOPlayer;
    private int badge;
    private int totalMatch;
    
    public Ranking() {
        super();
        badge = 0;
    }

    public Ranking(float winRate, int totalWinMatch, int scoreInTournament, float winRateWOPlayer, int badge, int totalMatch) {
        this.winRate = winRate;
        this.totalWinMatch = totalWinMatch;
        this.scoreInTournament = scoreInTournament;
        this.winRateWOPlayer = winRateWOPlayer;
        this.badge = badge;
        this.totalMatch = totalMatch;
    }

    public float getWinRate() {
        return winRate;
    }

    public void setWinRate(float winRate) {
        this.winRate = winRate;
    }

    public int getTotalWinMatch() {
        return totalWinMatch;
    }

    public void setTotalWinMatch(int totalWinMatch) {
        this.totalWinMatch = totalWinMatch;
    }

    public int getScoreInTournament() {
        return scoreInTournament;
    }

    public void setScoreInTournament(int scoreInTournament) {
        this.scoreInTournament = scoreInTournament;
    }

    public float getWinRateWOPlayer() {
        return winRateWOPlayer;
    }

    public void setWinRateWOPlayer(float winRateWOPlayer) {
        this.winRateWOPlayer = winRateWOPlayer;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getTotalMatch() {
        return totalMatch;
    }

    public void setTotalMatch(int totalMatch) {
        this.totalMatch = totalMatch;
    }
    
}
