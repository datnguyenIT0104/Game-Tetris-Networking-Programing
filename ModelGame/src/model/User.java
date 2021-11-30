package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author DatIT
 */
public class User implements Serializable{
    private static final long serialVersionUID = 20210811022L;
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final int PLAYING = 2;
    
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private Date birthday;
    private String role;
    private boolean isBanned;
    private int status;
    private ArrayList<Friend> listFriend;

    public User() {
        listFriend = new ArrayList<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        listFriend = new ArrayList<>();
    }

    public User(int id, String username, String password, String name, String email, Date birthday, String role, boolean isBanned, int status, ArrayList<Friend> listFriend) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.role = role;
        this.isBanned = isBanned;
        this.status = status;
        this.listFriend = listFriend;
    }

    public User(int id, String username, String name, String email, Date birthday, String role, boolean isBanned) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.role = role;
        this.isBanned = isBanned;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIsBanned() {
        return isBanned;
    }

    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public ArrayList<Friend> getListFriend() {
        return listFriend;
    }

    public void setListFriend(ArrayList<Friend> listFriend) {
        this.listFriend = listFriend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
