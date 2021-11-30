package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class Friend extends User implements Serializable{
    private static final long serialVersionUID = 20210811012L;
    public static final int HAS_NOT_REPLIED_YET = 1;
    public static final int ACCEPT = 2;
    public static final int REJEST = 3;
    public static final int DELETE = 4;
    
 
    private String relationship;
    private boolean isBlocked;
    private int performative;
    private String message;
    
    public Friend() {
        super();
        message = "";
    }
    public Friend(String relationship, boolean isBlocked, int performative, String message) {
       
        this.relationship = relationship;
        this.isBlocked = isBlocked;
        this.performative = performative;
        this.message = message;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public boolean isIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getPerformative() {
        return performative;
    }

    public void setPerformative(int performative) {
        this.performative = performative;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
