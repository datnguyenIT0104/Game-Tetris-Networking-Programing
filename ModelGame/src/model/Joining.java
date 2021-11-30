package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class Joining implements Serializable{
    private static final long serialVersionUID = 20210811015L;
    private int id;
    private String roleInGroup;
    private User user;
    private int score;
    
    public Joining() {
    }

    public Joining(int id, String roleInGroup, User user, int score) {
        this.id = id;
        this.roleInGroup = roleInGroup;
        this.user = user;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleInGroup() {
        return roleInGroup;
    }

    public void setRoleInGroup(String roleInGroup) {
        this.roleInGroup = roleInGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
