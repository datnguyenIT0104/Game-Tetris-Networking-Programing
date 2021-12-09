package tcp.client.control;

//import drawLine.GameForm;
import game.view.tetrisgame.GameFormClient;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.Group;
import model.IPAddress;
import model.ObjectWrapper;
import model.Ranking;
import model.Tournament;
import model.User;
import tcp.client.view.match.ChallengeFrm;
import tcp.client.view.group.CreateGroupFrm;
import tcp.client.view.tournament.CreateTournamentFrm;
import tcp.client.view.general.HomeFrm;
import tcp.client.view.group.JoinGroupFrm;
import tcp.client.view.tournament.JoinTournamentFrm;
import tcp.client.view.general.LoginFrm;
import tcp.client.view.general.PlayerDetailsFrm;
import tcp.client.view.ranking.RankingWOPlayerFrm;
import tcp.client.view.general.RegisterFrm;
import tcp.client.view.group.InvitationFrm;
import tcp.client.view.group.PlayInGroupFrm;
import tcp.client.view.match.ManageModeFrm;
import tcp.client.view.ranking.RankingBTWinMatchFrm;
import tcp.client.view.tournament.PlayInTournamentFrm;

/**
 *
 * @author DatIT
 */
public class ClientCtr {

    private Socket mySocket;
    private IPAddress serverAddress = new IPAddress("localhost", 2222);
    private ArrayList<ObjectWrapper> myFuntion;
    private JFrame form;
    private ClientListening myListening;

    public ClientCtr(JFrame form) {
        this.form = form;
        myFuntion = new ArrayList<>();

    }

    public boolean openConnection() {

        try {
            serverAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            mySocket = new Socket(serverAddress.getHost(), serverAddress.getPort());
            myListening = new ClientListening();
            myListening.start();
            JOptionPane.showMessageDialog(form, "Connect successfull to server at host: " + serverAddress.getHost()
                    + ", port: " + serverAddress.getPort());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error when connecting to server", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean sendData(Object obj) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error when sending data to server", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean closeConnnection() {
        try {
            if (myListening != null) {
                myListening.stop();
//                myListening = null;
            }
            if (mySocket != null) {
                mySocket.close();
                mySocket = null;
                JOptionPane.showMessageDialog(form, "Disconnected from server");
            }
            myFuntion.clear();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form, "Error when closing connection ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public ArrayList<ObjectWrapper> getMyFuntion() {
        return myFuntion;
    }

    class ClientListening extends Thread {

        public ClientListening() {
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());

                    Object reData = ois.readObject();                                           // doc du lieu server gui
                    if (reData instanceof ObjectWrapper) {
                        ObjectWrapper data = (ObjectWrapper) reData;

                        switch (data.getPerformative()) {
                            case ObjectWrapper.SERVER_INFORM_CLIENT_ONLINE:
                                // server gui danh sach nguoi dang online
                                if (form instanceof HomeFrm) {
                                    HomeFrm homeView = (HomeFrm) getForm();
                                    homeView.setListUsersOnline((ArrayList<User>) data.getData());
                                    homeView.fillRanking();

                                } else {
//                                    LoginFrm loginView = (LoginFrm) getForm();
//                                    loginView.setListUsersOnline((ArrayList<User>) data.getData());
                                }
                                break;
                            case ObjectWrapper.REPLY_LOGIN_USER:
                                if (form instanceof LoginFrm) {
                                    LoginFrm loginView = (LoginFrm) form;
                                    loginView.receiveDataProcessing(data);

                                }
                                break;
                            case ObjectWrapper.REPLY_RANKING:
                                // server tra kq ranking
                                ((HomeFrm) form).setListRankings((ArrayList<Ranking>) data.getData());
                                ((HomeFrm) form).updateStatus();

                                break;
                            case ObjectWrapper.REPLY_FRIEND_OF_USER:
                                ((HomeFrm) form).receiveDataProcessing(data);

                                break;
                            case ObjectWrapper.REPLY_GROUP_JOINED:
                                ((HomeFrm) form).setListGroupsJoined((ArrayList<Group>) data.getData());
                                ((HomeFrm) form).fillGroupJoined();

                                break;
                            case ObjectWrapper.REPLY_UPDATE_GROUP_TO_ALL_CLIENT:
                                if (form instanceof HomeFrm) {
                                    ((HomeFrm) form).getGroupJoined();
                                }

                                break;
                            case ObjectWrapper.SERVER_INFORM_LIST_REQUEST_FRIEND:
                                ((HomeFrm) form).setListFriendRequest((ArrayList<User>) data.getData());
                                ((HomeFrm) form).fillRequestFriend();
                                break;

                            case ObjectWrapper.REPLY_GET_LIST_REQUEST_FRIEND:
                                ((HomeFrm) form).setListFriendRequest((ArrayList<User>) data.getData());
                                ((HomeFrm) form).fillRequestFriend();
                                break;
                            case ObjectWrapper.REPLY_ACCEPT_REQUEST_FRIEND:
                                ((HomeFrm) form).receiveDataProcessing(data);

                                break;
                            case ObjectWrapper.REPLY_REMOVE_REQUEST_FRIEND:
                                ((HomeFrm) form).receiveDataProcessing(data);

                                break;
                            case ObjectWrapper.REPLY_REJECT_REQUEST_FRIEND:
                                ((HomeFrm) form).receiveDataProcessing(data);

                                break;
                            case ObjectWrapper.REPLY_UNFRIEND:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                break;
                            case ObjectWrapper.SERVER_INFORM_UNFRIEND:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                break;
                            case ObjectWrapper.SERVER_SEND_CHALLENGE_COMMUNICATE:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                break;
                            case ObjectWrapper.FREE_WAITING:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                break;
                            case ObjectWrapper.SERVER_SEND_ACCEPT_CHALLENGE_COMMUNICATE:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                break;
                            case ObjectWrapper.SERVER_INFORM_RESULT_MATCH:
                                removeGameFormInFuntionActive();
                                ((HomeFrm) form).receiveDataProcessing(data);
                                break;
                            case ObjectWrapper.SERVER_INFORM_ALL_UPDATE_RANK:

                                sendData(new ObjectWrapper(ObjectWrapper.RANKING, "winrate"));
//                                ((HomeFrm) form).updateStatus();
                                break;
                            case ObjectWrapper.REPLY_LEAVE_GROUP:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                
                                break;
                            case ObjectWrapper.SERVER_INFORM_UPDATE_GROUP:
                                 ((HomeFrm) form).receiveDataProcessing(data);
                                 
                                break;      
                            case ObjectWrapper.REPLY_GET_INFO_OF_PLAYER_IN_GROUP:
                                if(((HomeFrm) form).getPlayInGroupFrm() != null){
                                    ( ((HomeFrm) form).getPlayInGroupFrm()).receiveDataProcessing(data);
                                }

                                break;
                            case ObjectWrapper.SERVER_INFORM_MESSAGE_OF_GROUP:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                 
                                break;
                            case ObjectWrapper.REPLY_GET_MESSAGE_OF_GROUP:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                 
                                break;
                            case ObjectWrapper.REPLY_SEND_INVITATION_GROUP:
                                ( ((HomeFrm) form).getPlayInGroupFrm()).receiveDataProcessing(data);

                                break;
                            case ObjectWrapper.REPLY_JOIN_GROUP_BY_INVITATION:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                
                                break;
                            case ObjectWrapper.FRIEND_SEND_MESSAGE:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                
                                break;
                            case ObjectWrapper.REPLY_KICK_OUT_GROUP:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                
                                break;
                            case ObjectWrapper.REPLY_CHECK_OUT_GAME_BEFORE:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                
                                break;
                            case ObjectWrapper.STATUS_OF_ENEMY:
                                ((HomeFrm) form).receiveDataProcessing(data);
                                
                                break;
                        }

                        for (ObjectWrapper funtionActive : myFuntion) {
                            if (funtionActive.getPerformative() == data.getPerformative()) {
//                                    
                                switch (data.getPerformative()) {
                                    case ObjectWrapper.REPLY_REGISTER_ACCOUNT:
                                        RegisterFrm rV = (RegisterFrm) funtionActive.getData();
                                        rV.receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_CREATE_GROUP:
                                        CreateGroupFrm createGrF = (CreateGroupFrm) funtionActive.getData();
                                        createGrF.receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_JOIN_GROUP:
                                        JoinGroupFrm jgf = (JoinGroupFrm) funtionActive.getData();
                                        jgf.receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_REQUEST_FRIEND:
                                        PlayerDetailsFrm playerDF = (PlayerDetailsFrm) funtionActive.getData();
                                        playerDF.receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_CREATE_TOURNAMENT:
                                        ((CreateTournamentFrm) funtionActive.getData()).receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_GET_TOURNAMENT:
                                        JoinTournamentFrm jtf = (JoinTournamentFrm) funtionActive.getData();
                                        jtf.receiveDataProcessing(data);
                                        removeJoinTournamentFrm();
                                        
                                        break;
                                    case ObjectWrapper.REPLY_JOIN_TO_THE_TOURNAMENT:
                                        ((JoinTournamentFrm) funtionActive.getData()).receiveDataProcessing(data);
                                        removeJoinTournamentFrm();
                                        
                                        break;
                                    case ObjectWrapper.REPLY_GET_MODE:
//                                        removeChallengeFrm();
//                                        System.out.println("Mode");
                                        ((ChallengeFrm) funtionActive.getData()).receiveDataProcessing(data);
                                        
                                        break;
                                    case ObjectWrapper.REPLY_CHALLENGE_COMMUNICATE:

                                        ((ChallengeFrm) funtionActive.getData()).receiveDataProcessing(data);
                                        break;
                                    case ObjectWrapper.SEND_INFO_TO_CLIENT:
//                                        ((GameForm) funtionActive.getData()).receiveDataProcessing(data);
                                        ((GameFormClient) funtionActive.getData()).receiveDataProcessing(data);

                                        break;

                                    case ObjectWrapper.REPLY_RANKING_WITH_OTHER_PLAYER:
                                        ((RankingWOPlayerFrm) funtionActive.getData()).receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_RANKING_BY_TOTAL_WIN_MATCH:
                                        ((RankingBTWinMatchFrm) funtionActive.getData()).receiveDataProcessing(data);

                                        break;
                                    case ObjectWrapper.REPLY_GET_LIST_USER_ONLINE:
                                        ((PlayInTournamentFrm) funtionActive.getData()).receiveDataProcessing(data);
                                        removePlayInTournamentFrm();
                                        
                                        break;
                                    case ObjectWrapper.REPLY_GET_INFO_OF_PLAYER_IN_TOURNAMENT:
                                        ((PlayInTournamentFrm) funtionActive.getData()).receiveDataProcessing(data);
                                        removePlayInTournamentFrm();
                                        
                                        break;
                                    case ObjectWrapper.REPLY_GET_INVITATION_FROM_SERVER:
                                        ((InvitationFrm)funtionActive.getData()).receiveDataProcessing(data);
                                        
                                        break;
                                    case ObjectWrapper.REPLY_ACCESS_MODE:
                                        ((ManageModeFrm) funtionActive.getData()).receiveDataProcessing(data);
                                        
                                        break;
                                }
                                break;
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(form, "Error when receive data from server", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    public void removeGameFormInFuntionActive() {
        for (int i = 0; i < myFuntion.size(); i++) {
            JFrame form = (JFrame) myFuntion.get(i).getData();

            if (form instanceof GameFormClient) {
                myFuntion.remove(i);
                break;
            }
//            if (form instanceof GameForm) {
//                myFuntion.remove(i);
//                break;
//            }
        }
    }
    public void removeChallengeFrm(){
        for (int i = 0; i < myFuntion.size(); i++) {
            JFrame form = (JFrame) myFuntion.get(i).getData();
            
            if( form instanceof ChallengeFrm){
                myFuntion.remove(i);
                break;
            }
        }
    }
    public void removeJoinTournamentFrm(){
        for (int i = 0; i < myFuntion.size(); i++) {
            JFrame form = (JFrame) myFuntion.get(i).getData();

            if (form instanceof JoinTournamentFrm) {
                myFuntion.remove(i);
                break;
            }
        }
    }
    public void removePlayInTournamentFrm(){
        for (int i = 0; i < myFuntion.size(); i++) {
            JFrame form = (JFrame) myFuntion.get(i).getData();

            if (form instanceof PlayInTournamentFrm) {
                myFuntion.remove(i);
                break;
            }
        }
    }
    public JFrame getForm() {
        return form;
    }

    public void setForm(JFrame form) {
        this.form = form;
    }

}
