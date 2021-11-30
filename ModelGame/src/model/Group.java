package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author DatIT
 */
public class Group implements Serializable{
    private static final long serialVersionUID = 20210811013L;
    private int id;
    private String name;
    private int numMember;
    private int minimumBadge;
    private ArrayList<Joining> listJoining;
    private String message;
    
    public Group() {
        listJoining = new ArrayList<>();
    }

    public Group(int id, String name, int numMember, int minimumBadge, ArrayList<Joining> listJoining, String message) {
        this.id = id;
        this.name = name;
        this.numMember = numMember;
        this.minimumBadge = minimumBadge;
        this.listJoining = listJoining;
        this.message = message;
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

    public int getNumMember() {
        return numMember;
    }

    public void setNumMember(int numMember) {
        this.numMember = numMember;
    }

    public int getMinimumBadge() {
        return minimumBadge;
    }

    public void setMinimumBadge(int minimumBadge) {
        this.minimumBadge = minimumBadge;
    }

    public ArrayList<Joining> getListJoining() {
        return listJoining;
    }

    public void setListJoining(ArrayList<Joining> listJoining) {
        this.listJoining = listJoining;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
