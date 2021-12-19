package tcp.server.control;

import game.view.tetrisgame.GameForm;
import game.view.tetrisgame.GameFormServer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.net.ssl.SSLServerSocket;
import javax.swing.JOptionPane;

import model.Friend;
import model.Group;
import model.IPAddress;
import model.Joining;
import model.Match;
import model.Mode;
import model.ObjectWrapper;
import model.Ranking;
import model.Result;
import model.Tournament;
import model.User;
import tcp.server.view.ServerTCPHomeFrm;

/**
 *
 * @author DatIT
 */
public class TCPServerCtr {

    private ServerSocket myServerTCP;
    private DatagramSocket myClientUDP;
    private ServerTCPHomeFrm sHomeF;

    private ArrayList<ServerProcessing> listProcessing;
    private ServerListenning myListen;
    private ArrayList<User> listUsersOnline;
    private ArrayList<User> listFriendRequests;
    private ArrayList<Group> listGroup;
    private ArrayList<Group> listInvitation;
    private ArrayList<GameForm> listPlaying;
    private ArrayList<User> listReport;
    
    private IPAddress myAddress = new IPAddress("localhost", 2222);
    private IPAddress myAddressUDP = new IPAddress("localhost", 2223);
    private IPAddress serverAddress = new IPAddress("localhost", 5555);

    public TCPServerCtr(ServerTCPHomeFrm sHomeF) {
        this.sHomeF = sHomeF;
        listProcessing = new ArrayList<>();
        listUsersOnline = new ArrayList<>();
        listFriendRequests = new ArrayList<>();
        listGroup = new ArrayList<>();
        listInvitation = new ArrayList<>();
        listPlaying = new ArrayList<>();
        listReport = new ArrayList<>();
        
        openConnect();
    }

    public TCPServerCtr(ServerTCPHomeFrm sHomeF, int port) {
        this.sHomeF = sHomeF;
        myAddress.setPort(port);
        listProcessing = new ArrayList<>();
        listUsersOnline = new ArrayList<>();
        listFriendRequests = new ArrayList<>();
        listGroup = new ArrayList<>();
        listInvitation = new ArrayList<>();
        listPlaying = new ArrayList<>();
        listReport = new ArrayList<>();
        
        openConnect();
    }

    private void openConnect() {
        try {
            myServerTCP = new ServerSocket(myAddress.getPort());
            myClientUDP = new DatagramSocket(myAddressUDP.getPort());

            myListen = new ServerListenning();
            myListen.start();
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            sHomeF.showInforServer(myAddress);

            sHomeF.addMessage("Tetris Game TCP Server is listening on port " + myAddress.getPort() + " ...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            for (ServerProcessing sp : listProcessing) {
                sp.stop();
            }
            myListen.stop();
            myServerTCP.close();
            myClientUDP.close();
            sHomeF.addMessage("Game Tetris TCP server is close!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getListUsersOnline() {
        return listUsersOnline;
    }

    public void setListUsersOnline(ArrayList<User> listUsersOnline) {
        this.listUsersOnline = listUsersOnline;
    }

    public ArrayList<User> getListFriendRequests() {
        return listFriendRequests;
    }

    public void setListFriendRequests(ArrayList<User> listFriendRequests) {
        this.listFriendRequests = listFriendRequests;
    }

    public ArrayList<GameForm> getListPlaying() {
        return listPlaying;
    }

    public void setListPlaying(ArrayList<GameForm> listPlaying) {
        this.listPlaying = listPlaying;
    }

    public ServerTCPHomeFrm getsHomeF() {
        return sHomeF;
    }

    class ServerListenning extends Thread {// lắng nghe các client connect đến. sau đó tạo thread riêng để kết nối

        public ServerListenning() {
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket clientSocket = myServerTCP.accept();
                    ServerProcessing sp = new ServerProcessing(clientSocket);
                    sp.start();
                    listProcessing.add(sp);
                    sHomeF.addMessage("Number of client connecting to server: " + listProcessing.size());

                    // gui user dang online
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void publicUserOnline() {

        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_ONLINE, listUsersOnline);
        if (listProcessing.isEmpty()) {
            return;
        }
        for (ServerProcessing myProcessing : listProcessing) {
            myProcessing.sendData(ow);
        }
    }

    public void publicRankingNew() {

        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_ALL_UPDATE_RANK, "ok");
        for (ServerProcessing myProcessing : listProcessing) {
            myProcessing.sendData(ow);
        }
    }
    public void publicUpdateInforFriend(){
        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_UPDATE_INFOR_FRIEND, "ok");
        for (ServerProcessing myProcessing : listProcessing) {
            myProcessing.sendData(ow);
        }
    }

    public void publicGroup(User u) {
        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.REPLY_UPDATE_GROUP_TO_ALL_CLIENT, "UpdateGroupToAll");
        for (ServerProcessing myProcessing : listProcessing) {
            if (myProcessing.getUserOfPro().getUsername().equals(u.getUsername())) {
                continue;
            }

            myProcessing.sendData(ow);
        }
    }

    public void publicUpdateMemberGroup(Group group) {

        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_UPDATE_GROUP, group);
        for (ServerProcessing myProcessing : listProcessing) {
//            if( myProcessing.getUserOfPro().getId() == user.getId())
//                continue;

            myProcessing.sendData(ow);
        }
    }

    public void publicRankOfGroup(Match match) {
        Group group = match.getGroup();
        User u1 = match.getListResult().get(0).getUser();
        User u2 = match.getListResult().get(1).getUser();

        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_UPDATE_GROUP, group);
        for (ServerProcessing myProcessing : listProcessing) {
            if (myProcessing.getUserOfPro().getId() == u1.getId()
                    || myProcessing.getUserOfPro().getId() == u2.getId()) {
                continue;
            }

            myProcessing.sendData(ow);
        }
    }

    public void publicListRequestFriend() {
        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_LIST_REQUEST_FRIEND, listFriendRequests);
        for (ServerProcessing myProcessing : listProcessing) {

            myProcessing.sendData(ow);
        }
    }

    public void publicSendCommunicate(ObjectWrapper ow, User userReceive) {// gui thong diep den nguoi choi khac

        for (ServerProcessing sp : listProcessing) {
            if ( sp.getUserOfPro() != null && sp.getUserOfPro().getId() == userReceive.getId()) {
                sp.sendData(ow);
                return;
            }
        }

    }

    public void synchronizeMessageOfGroup(Group group, User user) {

        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_MESSAGE_OF_GROUP, group);
        for (ServerProcessing myProcessing : listProcessing) {
            if (myProcessing.getUserOfPro().getId() == user.getId()) {
                continue;
            }

            myProcessing.sendData(ow);
        }
    }

    public void informUnfriend(Friend f) {
        ObjectWrapper ow = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_UNFRIEND, "Unfriend");

        for (ServerProcessing myProcessing : listProcessing) {
            if (myProcessing.getUserOfPro().getId() == f.getId()) {
                myProcessing.sendData(ow);
                return;
            }
        }
    }

    public void saveResultOf2GameForm(Match match) {
        Match resultM = winlose(match);
        // luu ket qua
        sendDataUDP(new ObjectWrapper(ObjectWrapper.ADD_MATCH, resultM));
        sendDataUDP(new ObjectWrapper(ObjectWrapper.ADD_RESULT, resultM));
        // thong bao cap nhat bxh
        publicRankingNew();
    }

    public Match winlose(Match match) {
        Result receiveUser = match.getListResult().get(0);
        Result sendUser = match.getListResult().get(1);
        if (receiveUser.getOutcome() != -1) {
            return match;
        } else {
            if (receiveUser.getScore() >= sendUser.getScore()) {
                match.getListResult().get(0).setOutcome(Result.WIN);
                match.getListResult().get(1).setOutcome(Result.LOSE);

            } else {
                match.getListResult().get(0).setOutcome(Result.LOSE);
                match.getListResult().get(1).setOutcome(Result.WIN);
            }
            return match;
        }
    }

    public void restoreGame(Match match, User user) {
        listPlaying.add(new GameFormServer(match, user, this));
    }

    public boolean sendDataUDP(ObjectWrapper ow) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ow);
            oos.flush();

            // dong goi roi gui di
            byte[] sendData = baos.toByteArray();
//            System.out.println("Size UDP sending: " + sendData.length);
            DatagramPacket dp = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(serverAddress.getHost()), serverAddress.getPort());

            myClientUDP.send(dp);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(sHomeF, "Fail when sending datagram packet to server", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public ObjectWrapper receiveDataUDP() {
        ObjectWrapper result = null;
        try {
            byte[] buffer = new byte[131072];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            myClientUDP.receive(datagramPacket);

            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            result = (ObjectWrapper) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(sHomeF, "Error when receive data from server");
        }
        return result;
    }

    class ServerProcessing extends Thread {

        private Socket mySocket;
        private User userOfPro;

        public ServerProcessing(Socket mySocket) {
            this.mySocket = mySocket;
        }

        public void sendData(Object obj) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
                oos.writeObject(obj);
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
                    Object recivedData = ois.readObject();
                    int index = 0;
                    if (recivedData instanceof ObjectWrapper) {

                        ObjectWrapper data = (ObjectWrapper) recivedData;
                        ObjectWrapper dataReceiveUDP = new ObjectWrapper();
                        switch (data.getPerformative()) {

                            case ObjectWrapper.LOGIN_USER:// kiem tra dang nhap
                                User user = (User) data.getData();

                                boolean isLogined = false;
                                for (User u : listUsersOnline) {
                                    if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                                        System.out.println("Login roi");
                                        isLogined = true;
                                        break;
                                    }
                                }
                                if (isLogined) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_LOGIN_USER, "logined"));
                                    break;
                                }

                                sendDataUDP(data);
                                dataReceiveUDP = receiveDataUDP();

                                if (dataReceiveUDP.getData() instanceof User) {
                                    if( ((User)dataReceiveUDP.getData()).isIsBanned() ){
                                        dataReceiveUDP.setData("false");
                                    }else{
                                        listUsersOnline.add((User) dataReceiveUDP.getData());
                                        setUserOfPro((User) dataReceiveUDP.getData());
                                        
                                    }
                                    oos.writeObject(dataReceiveUDP);
                                    // gui danh sach useronline
                                    publicUserOnline();
                                } else {
                                    oos.writeObject(dataReceiveUDP);
                                }
                                break;

                            case ObjectWrapper.REGISTER_ACCOUNT:// dang ki tai khoan
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.RANKING:// lay xep hang
                                sendDataUDP(data);

                                dataReceiveUDP = receiveDataUDP();
                                oos.writeObject(dataReceiveUDP);
                                // co the them loai xep hang khach vao
                                break;
                            case ObjectWrapper.FRIEND_OF_USER:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.CREATE_GROUP:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.GROUP_JOINED:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.JOIN_GROUP:
                                sendDataUDP(data);
                                dataReceiveUDP = receiveDataUDP();
                                if (dataReceiveUDP.getData() instanceof Group) {

                                    deleteInvitation((Group) dataReceiveUDP.getData());
                                }

                                oos.writeObject(dataReceiveUDP);
                                break;
                            case ObjectWrapper.JOIN_GROUP_BY_INVITATION:
                                sendDataUDP(data);
                                dataReceiveUDP = receiveDataUDP();
                                if (dataReceiveUDP.getData() instanceof Group) {

                                    deleteInvitation((Group) dataReceiveUDP.getData());
                                }

                                oos.writeObject(dataReceiveUDP);
                                break;
                            case ObjectWrapper.UPDATE_GROUP_TO_ALL_CLIENT:
                                // cap nhat nhom o trang chu cho nguoi moi tham gia
                                data.setPerformative(ObjectWrapper.GROUP_JOINED);
                                data.setData(userOfPro);
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                // cap nhat thong tin nhom o trang chu cho nguoi choi khac
                                publicGroup(userOfPro);
                                break;
                            case ObjectWrapper.RANKING_WITH_OTHER_PLAYER:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.RANKING_BY_TOTAL_WIN_MATCH:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;

                            case ObjectWrapper.REQUEST_FRIEND:// gui yeu cau kb
                                listFriendRequests.add((User) data.getData());

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_REQUEST_FRIEND, "ok"));

                                publicListRequestFriend();
                                break;
//                                
                            case ObjectWrapper.GET_LIST_REQUEST_FRIEND:                                         // lay danh sach yeu cau kb

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.SERVER_INFORM_LIST_REQUEST_FRIEND, listFriendRequests));

                                break;
                            case ObjectWrapper.ACCEPT_REQUEST_FRIEND:                                           // chap nhan yeu cau them ban be
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                System.out.println("OK");
                                break;
                            case ObjectWrapper.UPDATE_LIST_FRIEND_REQUEST:
//                                setListFriendRequests((ArrayList<User>) data.getData());
                                for (int i = 0; i < listFriendRequests.size(); i++) {
                                    User u1 = listFriendRequests.get(i);

                                    Friend friend = u1.getListFriend().get(0);
                                    if (u1.getId() == ((User) data.getData()).getId() && friend.getId() == userOfPro.getId()) {
                                        listFriendRequests.set(i, (User) data.getData());
                                        break;
                                    }
                                }
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));
                                publicListRequestFriend();

                                break;
                            case ObjectWrapper.REMOVE_REQUEST_FRIEND:

                                listFriendRequests = (ArrayList<User>) data.getData();
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_REMOVE_REQUEST_FRIEND, "ok"));
                                publicListRequestFriend();
                                break;
                            case ObjectWrapper.REJECT_REQUEST_FRIEND:

                                listFriendRequests = (ArrayList<User>) data.getData();
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_REJECT_REQUEST_FRIEND, "ok"));
                                publicListRequestFriend();
                                break;
                            case ObjectWrapper.UNFRIEND:
                                sendDataUDP(data);
                                dataReceiveUDP = receiveDataUDP();
                                informUnfriend((Friend) dataReceiveUDP.getData());
                                oos.writeObject(dataReceiveUDP);
                                break;

                            case ObjectWrapper.CREATE_TOURNAMENT:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.GET_TOURNAMENT:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.JOIN_TO_THE_TOURNAMENT:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.GET_INFO_OF_PLAYER_IN_TOURNAMENT:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.GET_LIST_USER_ONLINE:

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_USER_ONLINE, listUsersOnline));

                                break;

                            case ObjectWrapper.GET_MODE:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.CHALLENGE_COMMUNICATE:                   // phia nguoi gui loi thach dau

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHALLENGE_COMMUNICATE, "ok"));

                                publicSendCommunicate(new ObjectWrapper(ObjectWrapper.SERVER_SEND_CHALLENGE_COMMUNICATE,
                                        (Match) data.getData()), ((Match) data.getData()).getListResult().get(0).getUser());
                                break;
                            case ObjectWrapper.CHANGE_STATUS:
                                for (int i = 0; i < listUsersOnline.size(); i++) {
                                    User uChangeS = listUsersOnline.get(i);
                                    if (uChangeS.getId() == userOfPro.getId()) {
                                        listUsersOnline.get(i).setStatus((int) data.getData());

                                        break;
                                    }
                                }
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));
                                publicUserOnline();
                                break;
                            case ObjectWrapper.ACCEPT_CHALLENGE_COMMUNICATE:                // gui den nguoi thach dau
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));

                                publicSendCommunicate(new ObjectWrapper(ObjectWrapper.SERVER_SEND_ACCEPT_CHALLENGE_COMMUNICATE,
                                        (Match) data.getData()),
                                        ((Match) data.getData()).getListResult().get(1).getUser());
                                // thay doi trang thai nguoi choi
                                changeStatus((Match) data.getData(), User.PLAYING);
                                break;
                            case ObjectWrapper.SEND_RESULT_TO_SERVER:

                                // kiem tra xem ng choi co thoat game hay k?
                                Match myMatch1 = (Match) data.getData();
                                if (checkOPlayerOut(myMatch1)) {
//                                    System.out.println("Client gui du lieu");
                                    myMatch1 = winlose(myMatch1);
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.SERVER_INFORM_RESULT_MATCH, myMatch1));
                                    // luu du lieu

                                    sendDataUDP(new ObjectWrapper(ObjectWrapper.ADD_MATCH, myMatch1));
                                    sendDataUDP(new ObjectWrapper(ObjectWrapper.ADD_RESULT, myMatch1));
                                    // thay doi trang thai nguoi choi
                                    changeStatus(myMatch1, User.ONLINE);
                                    Thread.sleep(1500);
                                    // thong bao cap nhat bxh
                                    publicRankingNew();
                                } else {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));
                                    for (Result result : ((Match) data.getData()).getListResult()) {
                                        if (result.getUser().getId() != userOfPro.getId()) {

                                            publicSendCommunicate(new ObjectWrapper(ObjectWrapper.SEND_INFO_TO_CLIENT, ((Match) data.getData())),
                                                    result.getUser());
                                            break;
                                        }
                                    }
                                }
                                break;
                            case ObjectWrapper.SEND2_RESULT_TO_SERVER:
                                
                                // tinh toan ket qua
                                Match resultM = winlose((Match) data.getData());

                                // dong goi du lieu
                                ObjectWrapper resultMatch = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_RESULT_MATCH, resultM);

                                // gui kq cho 1 ng
                                oos.writeObject(resultMatch);

                                // gui kq cho nguoi con lai
                                if (!checkOPlayerOutRemove(resultM)) {
                                    for (Result r : resultM.getListResult()) {
                                        if (r.getUser().getId() != userOfPro.getId()) {
                                            publicSendCommunicate(resultMatch, r.getUser());
                                            break;
                                        }
                                    }
                                }
                                // luu du lieu
                                sendDataUDP(new ObjectWrapper(ObjectWrapper.ADD_MATCH, resultM));
                                sendDataUDP(new ObjectWrapper(ObjectWrapper.ADD_RESULT, resultM));

                                // thay doi trang thai nguoi choi
                                changeStatus((Match) data.getData(), User.ONLINE);
                                Thread.sleep(1500);
                                // thong bao cap nhat bxh
                                publicRankingNew();
                                break;
                            case ObjectWrapper.GET_INFO_OF_PLAYER_IN_GROUP:
                                if (data.getData() instanceof Group) {
                                    sendDataUDP(data);

                                    oos.writeObject(receiveDataUDP());
                                }

                                break;
                            case ObjectWrapper.LEAVE_GROUP:
                                sendDataUDP(data);

                                dataReceiveUDP = receiveDataUDP();
                                if (dataReceiveUDP.getData().equals("ok")) {
                                    // lay lai du lieu
                                    dataReceiveUDP.setData(data.getData());
                                    oos.writeObject(dataReceiveUDP);

                                    // cap nhat du lieu cho nguoi khac
                                    publicGroup(userOfPro);
                                } else {
                                    oos.writeObject(dataReceiveUDP);
                                }

                                break;
                            case ObjectWrapper.KICK_OUT_GROUP:
                                sendDataUDP(data);

                                dataReceiveUDP = receiveDataUDP();
                                if (dataReceiveUDP.getData().equals("ok")) {

                                    // cap nhat du lieu cho nguoi khac
                                    publicGroup(userOfPro);
                                    // lay lai du lieu
                                    Thread.sleep(100);
                                    dataReceiveUDP.setData(data.getData());
                                    oos.writeObject(dataReceiveUDP);

                                } else {
                                    oos.writeObject(dataReceiveUDP);
                                }

                                break;
                            case ObjectWrapper.UPDATE_MEMBER_OF_GROUP:
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "free"));
                                Thread.sleep(100);
                                publicUpdateMemberGroup((Group) data.getData());
                                break;
                            case ObjectWrapper.SEND_MESSAGE_OF_GROUP:
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));

                                boolean isExistGroup = false;
                                index = 0;
                                for (Group item : listGroup) {
                                    if (item.getId() == ((Group) data.getData()).getId()) {
                                        isExistGroup = true;

                                        break;
                                    }
                                    index++;
                                }
                                if (!isExistGroup) {
                                    listGroup.add((Group) data.getData());

                                    synchronizeMessageOfGroup((Group) data.getData(), userOfPro);
                                } else {// neu chua co thi them tin nhan vao chuoi
                                    String messageOld = listGroup.get(index).getMessage();

                                    messageOld = messageOld + ((Group) data.getData()).getMessage();

                                    listGroup.get(index).setMessage(messageOld);

                                    synchronizeMessageOfGroup(listGroup.get(index), userOfPro);
                                }

                                break;
                            case ObjectWrapper.GET_MESSAGE_OF_GROUP:
                                Group groupExist = null;
                                for (Group item : listGroup) {
                                    if (item.getId() == ((Group) data.getData()).getId()) {
                                        groupExist = item;

                                        break;
                                    }
                                }
                                if (groupExist != null) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_MESSAGE_OF_GROUP,
                                            groupExist.getMessage()));
                                } else {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_MESSAGE_OF_GROUP, ""));
                                }
                                break;
                            case ObjectWrapper.UPDATE_RANK_OF_GROUP:
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));
                                publicRankOfGroup((Match) data.getData());

                                break;
                            case ObjectWrapper.SEND_INVITATION_GROUP:
                                // kiem tra xem loi moi van con hay k
                                // neu con thi tra ve chua tl
                                boolean isSended = false;
                                Group invite = (Group) data.getData();
                                Friend inviteFriend = new Friend();
                                for (Group item : listInvitation) {
                                    if (item.getId() == invite.getId()) {
                                        User user1 = item.getListJoining().get(0).getUser();
                                        User user2 = invite.getListJoining().get(0).getUser();
                                        if (user1.getId() == user2.getId()
                                                && user1.getListFriend().get(0).getId() == user2.getListFriend().get(0).getId()) {
                                            isSended = true;
                                            inviteFriend = user2.getListFriend().get(0);
                                            break;
                                        }
                                    }
                                }
                                if (isSended) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_SEND_INVITATION_GROUP, inviteFriend));
                                } else {
                                    listInvitation.add(invite);
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_SEND_INVITATION_GROUP, "ok"));
                                }

                                break;
                            case ObjectWrapper.GET_INVITATION_FROM_SERVER:

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_INVITATION_FROM_SERVER, listInvitation));
                                break;
                            case ObjectWrapper.DELETE_INVITATION:

                                deleteInvitation((Group) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));
                                break;
                            case ObjectWrapper.SEND_MESSAGE_TO_FRIEND:
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.FREE_WAITING, "ok"));

                                publicSendCommunicate(new ObjectWrapper(ObjectWrapper.FRIEND_SEND_MESSAGE, data.getData()), (User) data.getData());
                                break;
                            case ObjectWrapper.ACCESS_MODE:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                break;
//                            case ObjectWrapper.TEST_SEND_TETRISBLOCK:
//                                System.out.println("Gui du lieu thanh cong");
//                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_TEST_SEND_TETRISBLOCK, "ok"));
//                                break;
                            case ObjectWrapper.SEND_INFOR_ABOUT_PLAYING_GAME:
                                restoreGame((Match) data.getData(), userOfPro);
                                sHomeF.fillList(listPlaying);
                                informStatusOfEnemy((Match) data.getData(), "Your opponent has just exited the game!");
                                break;
                            case ObjectWrapper.CHECK_OUT_GAME_BEFORE:
                                boolean isPlaying = false;
                                index = 0;
                                Match myMatch = null;
                                for (GameForm item : listPlaying) {
                                    if (((GameFormServer) item).getMyAccount().getId() == userOfPro.getId()) {
                                        isPlaying = true;
                                        myMatch = ((GameFormServer) item).playerBack();
                                        break;
                                    }
                                    index++;
                                }
                                if (isPlaying) {
                                    listPlaying.remove(index);
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHECK_OUT_GAME_BEFORE, myMatch));
                                    for (int i = 0; i < listUsersOnline.size(); i++) {
                                        User item = listUsersOnline.get(i);
                                        if (item.getId() == userOfPro.getId()) {
                                            listUsersOnline.get(i).setStatus(User.PLAYING);
                                            break;
                                        }
                                    }
                                    sHomeF.fillList(listPlaying);
                                    informStatusOfEnemy(myMatch, "Your opponent is back!");
                                } else {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHECK_OUT_GAME_BEFORE,
                                            "no"));
                                }
                                break;
                            case ObjectWrapper.SEND_REPORT_TO_SERVER:
                                if( addReport((User) data.getData()))
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_SEND_REPORT_TO_SERVER, "ok"));
                                else
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_SEND_REPORT_TO_SERVER, "false"));
                                break;
                            case ObjectWrapper.GET_ALL_REPORT:
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_ALL_REPORT, listReport));
                                
                                break;
                            case ObjectWrapper.BAN_PLAYER:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                Thread.sleep(100);
                                publicSendCommunicate(new ObjectWrapper(ObjectWrapper.BANNED_BY_MANAGE, "closeYourGame"),
                                        (User) data.getData());
                                
                                Thread.sleep(100);
                                publicRankingNew();
                                Thread.sleep(100);
                                publicUpdateInforFriend();
                                
                                break;
                            case ObjectWrapper.UNBAN_PLAYER:
                                sendDataUDP(data);

                                oos.writeObject(receiveDataUDP());
                                Thread.sleep(100);
                                publicSendCommunicate(new ObjectWrapper(ObjectWrapper.UNBANNED_BY_MANAGE, "closeYourGame"),
                                        (User) data.getData());
                                Thread.sleep(100);
                                publicRankingNew();
                                Thread.sleep(100);
                                publicUpdateInforFriend();
                                break;
                            case ObjectWrapper.GET_ALL_TOURNAMENT:
                                sendDataUDP(data);
                                
                                oos.writeObject(receiveDataUDP());
                                break;
                            case ObjectWrapper.RANKING_BY_SCORE_IN_TOURNAMENT:
                                sendDataUDP(data);
                                
                                oos.writeObject(receiveDataUDP());
                                break;
                        }
                        oos.flush();
                    }
                }
            } catch (EOFException | SocketException ex) {

                listProcessing.remove(this);
                sHomeF.addMessage("Number of client connecting to server: " + listProcessing.size());
                // gui user dang online
                User u = null;

                for (User item : listUsersOnline) {
                    if (userOfPro != null && item.getId() == userOfPro.getId()) {
                        u = item;
                        break;
                    }
                }
                if (u != null) {
                    listUsersOnline.remove(u);
                }
                publicUserOnline();
                try {
                    mySocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public boolean addReport(User user){
            boolean isExist = false;
            for (User item : listReport) {
                if( item.getId() == user.getId()){
                    if( item.getListFriend().get(0).getId() == user.getListFriend().get(0).getId()){
                        isExist = true;
                        break;
                    }
                }
            }
            if( !isExist){
                
                listReport.add(user);
                return true;
            }else
                return false;
        }
        public void informStatusOfEnemy(Match myMatch, String msg) {
            // thong bao doi thu vua thoat game
            for (Result result : myMatch.getListResult()) {
                if (result.getUser().getId() != userOfPro.getId()) {

                    publicSendCommunicate(new ObjectWrapper(ObjectWrapper.STATUS_OF_ENEMY, msg),
                            result.getUser());
                    break;
                }
            }
        }

        public void deleteInvitation(Group group) {
            User myUser = group.getListJoining().get(0).getUser();

            for (int i = 0; i < listInvitation.size(); i++) {
                Group invite = listInvitation.get(i);
                User sender = invite.getListJoining().get(0).getUser();
                User receive = sender.getListFriend().get(0);
                if (group.getName().equals(invite.getName()) && receive.getId() == myUser.getId()) {
                    listInvitation.remove(i);
                }
            }

        }

        public void changeStatus(Match match, int status) {
            User receiveUser = match.getListResult().get(0).getUser();
            User sendUser = match.getListResult().get(1).getUser();
            for (int i = 0; i < listUsersOnline.size(); i++) {
                User u = listUsersOnline.get(i);
                if (sendUser.getId() == u.getId() || receiveUser.getId() == u.getId()) {
                    listUsersOnline.get(i).setStatus(status);
                }
            }
            publicUserOnline();
        }

        public void setUserOfPro(User userOfPro) {
            this.userOfPro = userOfPro;
        }

        public User getUserOfPro() {
            return userOfPro;
        }

        public boolean checkOPlayerOutRemove(Match match) {
            User receiver = match.getListResult().get(0).getUser();
            User sender = match.getListResult().get(1).getUser();
            for (int i = 0; i < listPlaying.size(); i++) {
                GameFormServer gfs = ((GameFormServer) listPlaying.get(i));
                // lay du lieu
                Match myMatch = gfs.getMyMatch();
                User receiver1 = myMatch.getListResult().get(0).getUser();
                User sender1 = myMatch.getListResult().get(1).getUser();

                if (receiver1.getId() == receiver.getId() && sender1.getId() == sender.getId()
                        && match.getPlayTime().equals(myMatch.getPlayTime())) {

                    listPlaying.remove(i);
                    // cap nhat ServerHome
                    sHomeF.fillList(listPlaying);
                    return true;
                }
            }
            return false;
        }

        public boolean checkOPlayerOut(Match match) {
            // kiem tra xem ng choi kia co out hay k?
            // Neu da out thi tinh ket qua luon tren server
            // Sau do luu vao csdl
            User receiver = match.getListResult().get(0).getUser();
            User sender = match.getListResult().get(1).getUser();
            for (int i = 0; i < listPlaying.size(); i++) {
                GameFormServer gfs = ((GameFormServer) listPlaying.get(i));
                // lay du lieu
                Match myMatch = gfs.getMyMatch();
                User receiver1 = myMatch.getListResult().get(0).getUser();
                User sender1 = myMatch.getListResult().get(1).getUser();

                if (receiver1.getId() == receiver.getId() && sender1.getId() == sender.getId()
                        && match.getPlayTime().equals(myMatch.getPlayTime())) {

                    match = gfs.receiveDataFromClient(match);
                    listPlaying.remove(i);
                    // cap nhat ServerHome
                    sHomeF.fillList(listPlaying);
                    return true;
                }
            }
            return false;
        }
    }

}
