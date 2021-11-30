package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author DatIT
 */
public class Tournament implements Serializable{
    private static final long serialVersionUID = 20210811020L;
    private int id;
    private String name;
    private Date createTime;
    private Date endDate;
    private int amount;
    private ArrayList<TournamentUser> listTournamentUsers;

    public Tournament() {
        listTournamentUsers = new ArrayList<>();
    }

    public Tournament(int id, String name, Date createTime, int amount) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.amount = amount;
    }

    public Tournament(int id, String name, Date createTime, Date endDate, int amount, ArrayList<TournamentUser> listTournamentUsers) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.endDate = endDate;
        this.amount = amount;
        this.listTournamentUsers = listTournamentUsers;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<TournamentUser> getListTournamentUsers() {
        return listTournamentUsers;
    }

    public void setListTournamentUsers(ArrayList<TournamentUser> listTournamentUsers) {
        this.listTournamentUsers = listTournamentUsers;
    }

}
