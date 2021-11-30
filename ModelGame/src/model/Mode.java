package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class Mode implements Serializable{
    private static final long serialVersionUID = 20210811017L;
    
    public static final int CREATE = 1;
    public static final int EDIT = 2;
    public static final int DELETE = 3;
    public static final int GET_ALL = 4;
    
    
    private int id;
    private int time;
    private int speed;
    private String name;
    private int performative;
    
    public Mode() {
    }

    public Mode(int id, int time, int speed, String name) {
        this.id = id;
        this.time = time;
        this.speed = speed;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPerformative() {
        return performative;
    }

    public void setPerformative(int performative) {
        this.performative = performative;
    }
    
}
