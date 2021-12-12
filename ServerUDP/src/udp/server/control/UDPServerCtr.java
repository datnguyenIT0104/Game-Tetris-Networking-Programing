package udp.server.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import jdbc.dao.FriendDAO;
import jdbc.dao.GroupDAO;
import jdbc.dao.JoiningDAO;
import jdbc.dao.MatchDAO;
import jdbc.dao.ModeDAO;
import jdbc.dao.RankingDAO;
import jdbc.dao.ResultDAO;
import jdbc.dao.TournamentDAO;
import jdbc.dao.TournamentUserDAO;
import jdbc.dao.UserDAO;
import model.Friend;
import model.Group;
import model.IPAddress;
import model.Joining;
import model.Match;
import model.Mode;
import model.ObjectWrapper;
import model.Ranking;
import model.Tournament;
import model.TournamentUser;
import model.User;
import udp.server.view.ServerUDPHomeFrm;

/**
 *
 * @author DatIT
 */
public class UDPServerCtr {

    private ServerUDPHomeFrm view;
    private DatagramSocket myServer;
    private IPAddress myAddress = new IPAddress("localhost", 5555); //default server address
    private UDPListening myListening;

    public UDPServerCtr(ServerUDPHomeFrm view) {
        this.view = view;
    }

    public UDPServerCtr(ServerUDPHomeFrm view, int port) {
        this.view = view;
        myAddress.setPort(port);
    }

    public boolean open() {
        try {
            myServer = new DatagramSocket(myAddress.getPort());
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showInforServer(myAddress);
            myListening = new UDPListening();
            myListening.start();
            view.addMessage("UDP server is running at the host: " + myAddress.getHost() + ", port: " + myAddress.getPort());
        } catch (Exception e) {
            e.printStackTrace();
            view.addMessage("Error to open the datagram socket!");
            return false;
        }
        return true;
    }

    public boolean close() {
        try {
            myListening.stop();
            myServer.close();
        } catch (Exception e) {
            e.printStackTrace();
            view.addMessage("Error to close the datagram socket!");
            return false;
        }
        return true;
    }

    class UDPListening extends Thread {
//        private ArrayList<User> listUserOnline;
//        private ArrayList<User> listRequestFriend;

        public UDPListening() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // chua bi bo dem
                    byte[] buffer = new byte[131072];
                    DatagramPacket rePacket = new DatagramPacket(buffer, buffer.length);
                    myServer.receive(rePacket);
                    // day vao luong byte vao chuyen thanh Object
                    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    ObjectWrapper receiveData = (ObjectWrapper) ois.readObject();

                    // xu ly du lieu
                    ObjectWrapper result = new ObjectWrapper();
                    switch (receiveData.getPerformative()) {
                        case ObjectWrapper.LOGIN_USER:
                            result.setPerformative(ObjectWrapper.REPLY_LOGIN_USER);
                            User user = (User) receiveData.getData();
                            if ((new UserDAO()).checkLogin(user)) {
                                user.setStatus(User.ONLINE);
                                result.setData(user);
//                                view.addMessage("Number of online client: " + listUserOnline.size());
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.REGISTER_ACCOUNT:
                            result.setPerformative(ObjectWrapper.REPLY_REGISTER_ACCOUNT);
                            if ((new UserDAO()).createAccount((User) receiveData.getData())) {
                                result.setData("ok");

                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.RANKING:
                            result.setPerformative(ObjectWrapper.REPLY_RANKING);
                            ArrayList<Ranking> rankWinRate = (new RankingDAO()).getRankingByWinRate();
                            result.setData(rankWinRate);

                            break;
                        case ObjectWrapper.FRIEND_OF_USER:
                            result.setPerformative(ObjectWrapper.REPLY_FRIEND_OF_USER);
                            ArrayList<Friend> listFriend = (new FriendDAO()).getFriend((User) receiveData.getData());

                            result.setData(listFriend);
                            break;
                        case ObjectWrapper.GROUP_JOINED:
                            result.setPerformative(ObjectWrapper.REPLY_GROUP_JOINED);
                            ArrayList<Group> listGroup = (new GroupDAO()).getGroupJoined((User) receiveData.getData());

                            result.setData(listGroup);
                            break;
                        case ObjectWrapper.JOIN_GROUP:
                            result.setPerformative(ObjectWrapper.REPLY_JOIN_GROUP);
                            if (receiveData.getData() instanceof User) {
                                result.setData((new GroupDAO()).getGroupNotJoined((User) receiveData.getData()));
                            } else {
                                if ((new JoiningDAO()).joinGroup((Group) receiveData.getData())) {

                                    result.setData((Group) receiveData.getData());
                                } else {
                                    result.setData("false");
                                }
                            }

                            break;
                        case ObjectWrapper.CREATE_GROUP:
                            result.setPerformative(ObjectWrapper.REPLY_CREATE_GROUP);
                            Group g = (Group) receiveData.getData();
                            GroupDAO gdao = new GroupDAO();
                            JoiningDAO jdao = new JoiningDAO();
                            if (gdao.createGroup(g) && jdao.joinGroupAdmin(g)) {
                                result.setData("ok");
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.RANKING_WITH_OTHER_PLAYER:
                            result.setPerformative(ObjectWrapper.REPLY_RANKING_WITH_OTHER_PLAYER);
                            ArrayList<Ranking> listRankingWOP;
                            listRankingWOP = (new RankingDAO()).getRankingByRateWinWOPlayer((User) receiveData.getData());

                            result.setData(listRankingWOP);
                            break;
                        case ObjectWrapper.RANKING_BY_TOTAL_WIN_MATCH:
                            result.setPerformative(ObjectWrapper.REPLY_RANKING_BY_TOTAL_WIN_MATCH);
                            ArrayList<Ranking> listRankingBTWin;
                            listRankingBTWin = (new RankingDAO()).getRankingByTotalWinMatch();

                            result.setData(listRankingBTWin);
                            break;

                        case ObjectWrapper.GET_TOURNAMENT:
                            result.setPerformative(ObjectWrapper.REPLY_GET_TOURNAMENT);
                            if (receiveData.getData() instanceof Date) {
                                ArrayList<Tournament> listTournaments = (new TournamentDAO()).getTournaments((Date) receiveData.getData());
                                result.setData(listTournaments);
                            }
                            break;
                        case ObjectWrapper.CREATE_TOURNAMENT:
                            result.setPerformative(ObjectWrapper.REPLY_CREATE_TOURNAMENT);

                            if ((new TournamentDAO()).createTournament((Tournament) receiveData.getData())
                                    && (new TournamentUserDAO()).joinTournament((Tournament) receiveData.getData())) {
                                result.setData("ok");
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.JOIN_TO_THE_TOURNAMENT:
                            result.setPerformative(ObjectWrapper.REPLY_JOIN_TO_THE_TOURNAMENT);

                            if ((new TournamentUserDAO()).joinTournament((Tournament) receiveData.getData())) {
                                result.setData("ok");
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.GET_INFO_OF_PLAYER_IN_TOURNAMENT:
                            result.setPerformative(ObjectWrapper.REPLY_GET_INFO_OF_PLAYER_IN_TOURNAMENT);
                            ArrayList<TournamentUser> listTournamentUsers;
                            listTournamentUsers = (new TournamentUserDAO()).getPlayerInTournament((Tournament) receiveData.getData());

                            result.setData(listTournamentUsers);
                            break;
                        case ObjectWrapper.GET_LIST_USER_ONLINE:
//                            result.setPerformative(ObjectWrapper.SERVER_INFORM_CLIENT_ONLINE);
//                            
//                            result.setData(listUserOnline);
                            break;
                        case ObjectWrapper.CLIENT_OFFLINE:
//                            listUserOnline = (ArrayList<User>) receiveData.getData();
//                            
//                            view.addMessage("Number of online client: " + listUserOnline.size());
                            break;
                        case ObjectWrapper.REQUEST_FRIEND:
//                            result.setPerformative(ObjectWrapper.REPLY_REQUEST_FRIEND);
////                            
////                            listRequestFriend.add((User) receiveData.getData());
//                            result.setData("ok");
                            break;
                        case ObjectWrapper.GET_LIST_REQUEST_FRIEND:
//                            result.setPerformative(ObjectWrapper.REPLY_GET_LIST_REQUEST_FRIEND);
//                            
//                            result.setData(listRequestFriend);
                            break;
                        case ObjectWrapper.ACCEPT_REQUEST_FRIEND:
                            result.setPerformative(ObjectWrapper.REPLY_ACCEPT_REQUEST_FRIEND);
                            if ((new FriendDAO()).saveAddFriend((User) receiveData.getData())) {

                                result.setData((User) receiveData.getData());
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.UPDATE_LIST_FRIEND_REQUEST:
//                            listRequestFriend = (ArrayList<User>) receiveData.getData();
                            continue;

                        case ObjectWrapper.REMOVE_REQUEST_FRIEND:
//                            listRequestFriend = (ArrayList<User>) receiveData.getData();

                            continue;
                        case ObjectWrapper.REJECT_REQUEST_FRIEND:
//                            listRequestFriend = (ArrayList<User>) receiveData.getData();

                            continue;
                        case ObjectWrapper.UNFRIEND:
                            result.setPerformative(ObjectWrapper.REPLY_UNFRIEND);
                            if ((new FriendDAO()).deleteFriend((User) receiveData.getData())) {

                                result.setData(((User) receiveData.getData()).getListFriend().get(0));
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.GET_MODE:
                            result.setPerformative(ObjectWrapper.REPLY_GET_MODE);
                            ArrayList<Mode> listModes = ( new ModeDAO()).getAllMode();
//                            System.out.println("List mode: " + listModes.size());
                            result.setData(listModes);
                            break;
                        case ObjectWrapper.ADD_MATCH:
                            (new MatchDAO()).addMatch((Match) receiveData.getData());

                            continue;
                        case ObjectWrapper.ADD_RESULT:
                            // thua
                            // da sua
                            (new ResultDAO()).addResult((Match) receiveData.getData());

                            continue;

                        case ObjectWrapper.GET_INFO_OF_PLAYER_IN_GROUP:
                            result.setPerformative(ObjectWrapper.REPLY_GET_INFO_OF_PLAYER_IN_GROUP);
                            ArrayList<Joining> listJoinings = (new JoiningDAO()).getPlayerInGroup(
                                    (Group) receiveData.getData());

                            result.setData(listJoinings);
                            break;

                        case ObjectWrapper.LEAVE_GROUP:
                            result.setPerformative(ObjectWrapper.REPLY_LEAVE_GROUP);
                            Joining joining = ((Group) receiveData.getData()).getListJoining().get(0);

                            if ((new JoiningDAO()).leaveGroup(joining)) {
                                result.setData("ok");
                            } else {
                                result.setData("fasle");
                            }

                            break;
                        case ObjectWrapper.KICK_OUT_GROUP:
                            result.setPerformative(ObjectWrapper.REPLY_KICK_OUT_GROUP);
                            Joining joining1 = ((Group) receiveData.getData()).getListJoining().get(0);

                            if ((new JoiningDAO()).leaveGroup(joining1)) {
                                result.setData("ok");
                            } else {
                                result.setData("fasle");
                            }

                            break;
                        case ObjectWrapper.JOIN_GROUP_BY_INVITATION:
                            result.setPerformative(ObjectWrapper.REPLY_JOIN_GROUP_BY_INVITATION);

                            if ((new JoiningDAO()).joinGroup((Group) receiveData.getData())) {

                                result.setData((Group) receiveData.getData());
                            } else {
                                result.setData("false");
                            }
                            break;
                        case ObjectWrapper.ACCESS_MODE:
                            result.setPerformative(ObjectWrapper.REPLY_ACCESS_MODE);
                            if (receiveData.getData() instanceof Mode) {
                                Mode mode = (Mode) receiveData.getData();
                                if (mode.getPerformative() == Mode.CREATE) {
                                    if ((new ModeDAO()).createMode(mode)) {
                                        result.setData("Create Successfully");
                                    } else {
                                        result.setData("False");
                                    }
                                }else if( mode.getPerformative() == Mode.EDIT){
                                    if( (new ModeDAO()).updateMode(mode))
                                        result.setData("Edit successfully");
                                    else
                                        result.setData("False");
                                }else if( mode.getPerformative() == Mode.DELETE){
                                    if( (new ModeDAO()).deleteMode(mode))
                                        result.setData("Deleted");
                                    else
                                        result.setData("False");
                                }

                            }else if ((int) receiveData.getData() == Mode.GET_ALL) {
                                result.setData((new ModeDAO()).getAllMode());
                            }

                            break;
                    }

                    // tao mang dem 
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(result);
                    oos.flush();

                    // tao goi tin
                    byte[] sendData = baos.toByteArray();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, rePacket.getAddress(), rePacket.getPort());
                    myServer.send(sendPacket);

                } catch (Exception e) {
                    e.printStackTrace();
                    view.addMessage("Error when processing an incoming package");
                    view.addMessage(e.getMessage());
                }
            }
        }
    }

}
