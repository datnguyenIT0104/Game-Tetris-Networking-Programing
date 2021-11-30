package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author DatIT
 */
public class Match implements Serializable{
    private static final long serialVersionUID = 20210811016L;
    public static final int HAS_NOT_REPLIED_YET = 1;
    public static final int ACCEPT = 2;
    public static final int REJEST = 3;
    public static final int FINISH = 4;
    
    private int id;
    private String note;
    private Mode mode;
    private ArrayList<Result> listResult;
    private Date playTime;
    private int[] randomBlock;
    private Tournament tournament;
    private Group group;
    
    public Match() {
        listResult = new ArrayList<>();
    }

    public Match(int id, String note, Mode mode, ArrayList<Result> listResult, Date playTime, int[] randomBlock, Tournament tournament, Group group) {
        this.id = id;
        this.note = note;
        this.mode = mode;
        this.listResult = listResult;
        this.playTime = playTime;
        this.randomBlock = randomBlock;
        this.tournament = tournament;
        this.group = group;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public ArrayList<Result> getListResult() {
        return listResult;
    }

    public void setListResult(ArrayList<Result> listResult) {
        this.listResult = listResult;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public int[] getRandomBlock() {
        return randomBlock;
    }

    public void setRandomBlock(int[] randomBlock) {
        this.randomBlock = randomBlock;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
}
