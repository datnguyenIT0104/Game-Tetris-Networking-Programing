package model;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class ObjectWrapper implements Serializable{
    private static final long serialVersionUID = 20210811011L;

    
    public static final int LOGIN_USER = 1;
    public static final int REPLY_LOGIN_USER = 2;
    public static final int SERVER_INFORM_CLIENT_ONLINE = 3;
    public static final int REGISTER_ACCOUNT = 4;
    public static final int REPLY_REGISTER_ACCOUNT = 5;
    public static final int CLIENT_ONLINE = 6;
    public static final int FRIEND_OF_USER = 7;
    public static final int REPLY_FRIEND_OF_USER = 8;
    public static final int RANKING = 9;
    public static final int REPLY_RANKING = 10;
    public static final int CREATE_GROUP = 11;
    public static final int REPLY_CREATE_GROUP = 12;
    public static final int GROUP_JOINED = 13;
    public static final int REPLY_GROUP_JOINED = 14;
    public static final int JOIN_GROUP = 15;
    public static final int REPLY_JOIN_GROUP = 16;
    public static final int UPDATE_GROUP_TO_ALL_CLIENT = 17;
    public static final int REPLY_UPDATE_GROUP_TO_ALL_CLIENT = 18;
    public static final int GET_LIST_REQUEST_FRIEND = 19;
    public static final int REPLY_GET_LIST_REQUEST_FRIEND = 20;
    public static final int REQUEST_FRIEND = 21;
    public static final int REPLY_REQUEST_FRIEND = 22;
    public static final int SERVER_INFORM_LIST_REQUEST_FRIEND = 23;
    public static final int ACCEPT_REQUEST_FRIEND = 24;
    public static final int REPLY_ACCEPT_REQUEST_FRIEND = 25;
    public static final int REMOVE_REQUEST_FRIEND = 26;
    public static final int REPLY_REMOVE_REQUEST_FRIEND = 27;
    public static final int REJECT_REQUEST_FRIEND = 28;
    public static final int REPLY_REJECT_REQUEST_FRIEND = 29;
    public static final int UNFRIEND = 30;
    public static final int REPLY_UNFRIEND = 31;
    public static final int SERVER_INFORM_UNFRIEND = 32;
    public static final int CREATE_TOURNAMENT = 33;
    public static final int REPLY_CREATE_TOURNAMENT = 34;
    public static final int GET_TOURNAMENT = 35;
    public static final int REPLY_GET_TOURNAMENT = 36;
    public static final int GET_MODE = 37;
    public static final int REPLY_GET_MODE = 38;
    public static final int CHALLENGE_COMMUNICATE = 39;
    public static final int REPLY_CHALLENGE_COMMUNICATE = 40;
    public static final int SERVER_SEND_CHALLENGE_COMMUNICATE = 41;
    public static final int CHANGE_STATUS = 42;
    public static final int FREE_WAITING = 43;
    public static final int UPDATE_LIST_FRIEND_REQUEST = 44;
    public static final int ACCEPT_CHALLENGE_COMMUNICATE = 45;
    public static final int SERVER_SEND_ACCEPT_CHALLENGE_COMMUNICATE = 46;
    public static final int SEND_RESULT_TO_SERVER = 47;
    public static final int SEND_INFO_TO_CLIENT = 48;
    public static final int SEND2_RESULT_TO_SERVER = 49;
    public static final int SERVER_INFORM_RESULT_MATCH = 50;
    public static final int SERVER_INFORM_ALL_UPDATE_RANK = 51;
    public static final int START_GAME = 52;
    public static final int REPLY_START_GAME = 53;
    public static final int COMPETITOR_START_GAME = 54;
    public static final int RANKING_WITH_OTHER_PLAYER = 55;
    public static final int REPLY_RANKING_WITH_OTHER_PLAYER = 56;
    public static final int RANKING_BY_TOTAL_WIN_MATCH = 57;
    public static final int REPLY_RANKING_BY_TOTAL_WIN_MATCH = 58;
    public static final int RANKING_BY_SCORE_IN_TOURNAMENT = 59;
    public static final int REPLY_RANKING_BY_SCORE_IN_TOURNAMEN = 60;
    public static final int JOIN_TO_THE_TOURNAMENT = 61;
    public static final int REPLY_JOIN_TO_THE_TOURNAMENT = 62;
    public static final int GET_INFO_OF_PLAYER_IN_TOURNAMENT = 63;
    public static final int REPLY_GET_INFO_OF_PLAYER_IN_TOURNAMENT = 64;
    public static final int GET_LIST_USER_ONLINE = 65;
    public static final int CLIENT_OFFLINE = 66;
    public static final int REPLY_GET_LIST_USER_ONLINE = 67;
    public static final int ADD_MATCH = 68;
    public static final int ADD_RESULT = 69;
    public static final int GET_INFO_OF_PLAYER_IN_GROUP = 70;
    public static final int REPLY_GET_INFO_OF_PLAYER_IN_GROUP = 71;
    public static final int LEAVE_GROUP = 72;
    public static final int REPLY_LEAVE_GROUP = 73;
    public static final int SERVER_INFORM_UPDATE_GROUP = 74;
    public static final int UPDATE_MEMBER_OF_GROUP = 75;
    public static final int SEND_MESSAGE_OF_GROUP = 76;
    public static final int SERVER_INFORM_MESSAGE_OF_GROUP = 77;
    public static final int GET_MESSAGE_OF_GROUP = 78;
    public static final int REPLY_GET_MESSAGE_OF_GROUP = 79;
    public static final int UPDATE_RANK_OF_GROUP = 80;
    public static final int SEND_INVITATION_GROUP = 81;
    public static final int REPLY_SEND_INVITATION_GROUP = 82;
    public static final int GET_INVITATION_FROM_SERVER = 83;
    public static final int REPLY_GET_INVITATION_FROM_SERVER = 84;
    public static final int JOIN_GROUP_BY_INVITATION = 85;
    public static final int REPLY_JOIN_GROUP_BY_INVITATION = 86;
    public static final int DELETE_INVITATION = 87;
    public static final int SEND_MESSAGE_TO_FRIEND = 89;
    public static final int FRIEND_SEND_MESSAGE = 90;
    public static final int KICK_OUT_GROUP = 91;
    public static final int REPLY_KICK_OUT_GROUP = 92;
    public static final int ACCESS_MODE = 93;
    public static final int REPLY_ACCESS_MODE = 94;
    
    
    private int performative;
    private Object data;

    public ObjectWrapper() {
    }

    
    public ObjectWrapper(int performative, Object data) {
        this.performative = performative;
        this.data = data;
    }

    public int getPerformative() {
        return performative;
    }

    public void setPerformative(int performative) {
        this.performative = performative;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
