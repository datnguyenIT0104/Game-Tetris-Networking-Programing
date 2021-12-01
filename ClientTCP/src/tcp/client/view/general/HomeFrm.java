package tcp.client.view.general;

import tcp.client.view.ranking.RankingWOPlayerFrm;
import tcp.client.view.tournament.ManagerTournamentFrm;
import tcp.client.view.tournament.JoinTournamentFrm;
import tcp.client.view.match.ResultMatchFrm;
import tcp.client.view.group.JoinGroupFrm;
import tcp.client.view.group.CreateGroupFrm;
import game.view.tetrisgame.GameForm;
import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Friend;
import model.Group;
import model.Match;
import model.ObjectWrapper;
import model.Ranking;
import model.User;
import tcp.client.control.ClientCtr;
import tcp.client.view.group.InvitationFrm;
import tcp.client.view.group.PlayInGroupFrm;
import tcp.client.view.match.ManageModeFrm;
import tcp.client.view.match.ReceiveChallengeFrm;
import tcp.client.view.ranking.RankingBTWinMatchFrm;
import tcp.client.view.tournament.PlayInTournamentFrm;

/**
 *
 * @author DatIT
 */
public class HomeFrm extends javax.swing.JFrame {
    
    private ClientCtr myControl;

    // data hide
    private ArrayList<Ranking> listRankings;
    private ArrayList<Group> listGroupsJoined;
    private ArrayList<User> listUsersOnline;
    private ArrayList<User> listFriendRequest;
    private User myAccount;
    private PlayInGroupFrm playInGroupFrm;
    // data hide

    //model
    private DefaultTableModel modelRanking;
    private DefaultTableModel modelGroupsJoined;
    private DefaultTableModel modelFriend;
    private DefaultTableModel modelFriendRequest;
    
    public HomeFrm(ClientCtr myControl, User myAccount, ArrayList<User> listUsersOnline) {
        initComponents();
        this.myControl = myControl;
        this.myAccount = myAccount;
        this.listUsersOnline = listUsersOnline;
        // khoi tao list
        listRankings = new ArrayList<>();
        listGroupsJoined = new ArrayList<>();
        listFriendRequest = new ArrayList<>();

        // khoi tao form group
        playInGroupFrm = null;
        // khoi tao cac bang va danh sach
        initForm();
        // khoi tao gia tri bang
        getDataFromServer();

        // khoi tao thong tin ca nhan
        setProperties();
        
        this.pack();
//        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initForm() {
        initRanking();
        initFriend();
        initFriendRequest();
        initGroupJoined();
    }
    
    private void initRanking() {
        modelRanking = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        modelRanking.setColumnIdentifiers(new Object[]{
            "TOP", "USERNAME", "WIN RATE", "STATUS"
        });
        
        tblRanking.setModel(modelRanking);
    }

    private void initFriend() {
        modelFriend = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        modelFriend.setColumnIdentifiers(new Object[]{
            "USERNAME", "TOP"
        });
        tblFriend.setModel(modelFriend);
    }

    private void initFriendRequest() {
        modelFriendRequest = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        modelFriendRequest.setColumnIdentifiers(new Object[]{
            "USERNAME", "TOP"
        });
        tblFriendRequest.setModel(modelFriendRequest);
    }

    private void initGroupJoined() {
        modelGroupsJoined = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        int index = 1;
        modelGroupsJoined.setColumnIdentifiers(new Object[]{
            "INDEX", "NAME", "NUMBER OF MEMBERS"
        });
        modelGroupsJoined.setRowCount(0);
        tblGroupsJoined.setModel(modelGroupsJoined);
    }
    
    private void getDataFromServer() {
        myControl.sendData(new ObjectWrapper(ObjectWrapper.RANKING, "winrate"));
        myControl.sendData(new ObjectWrapper(ObjectWrapper.FRIEND_OF_USER, myAccount));
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GROUP_JOINED, myAccount));
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_REQUEST_FRIEND, "GetListRequest"));
    }
    
    public void getGroupJoined() {
        myControl.sendData(new ObjectWrapper(ObjectWrapper.GROUP_JOINED, myAccount));
        
    }
    
    public void fillRanking() {
        // lay du lieu
        modelRanking.setRowCount(0);
        int index = 1;
        
        for (Ranking rank : listRankings) {
            String status = "";
//            User uTemp = new User(rank.getId(), rank.getUsername(), rank.getName(), rank.getEmail(), rank.getBirthday(), rank.getRole(), rank.isIsBanned());
            for (User user : listUsersOnline) {
                if (rank.getUsername().equals(user.getUsername())) {
                    if (user.getStatus() == User.ONLINE) {
                        status = "online";
                    } else if (user.getStatus() == User.OFFLINE) {
                        status = "offline";
                    } else {
                        status = "playing";
                    }
                    break;
                }
            }            
            modelRanking.addRow(new Object[]{
                index++, rank.getUsername(), rank.getWinRate() + "%", status
            });
        }
        
        modelRanking.fireTableDataChanged();
    }

    public void fillFriend() {
        modelFriend.setRowCount(0);
        for (Friend friend : myAccount.getListFriend()) {
            int rank = 0;
            int index = 1;
            for (Ranking item : listRankings) {
                if (friend.getId() == item.getId()) {
                    rank = index;
                    break;
                }
                index++;
            }
            modelFriend.addRow(new Object[]{
                friend.getUsername(), index
            });
        }
    }

    public void fillGroupJoined() {
        modelGroupsJoined.setRowCount(0);
        int index = 1;
        for (Group g : listGroupsJoined) {
            modelGroupsJoined.addRow(new Object[]{
                index++, g.getName(), g.getNumMember()
            });
        }
        modelGroupsJoined.fireTableDataChanged();
    }
    
    public void fillRequestFriend() {
        // yeu cau ket ban
        modelFriendRequest.setRowCount(0);
        tblFriendRequest.removeAll();
        for (int j = 0; j < listFriendRequest.size(); j++) {
            
            User user = listFriendRequest.get(j);
            Friend friend = user.getListFriend().get(0);                        // them danh sach ban be
            if (friend.getId() == myAccount.getId() && friend.getPerformative() == Friend.HAS_NOT_REPLIED_YET) {
                int rank = 1;
                for (Ranking r : listRankings) {
                    if (r.getId() == user.getId()) {
                        
                        break;
                    }
                    rank++;
                }
                modelFriendRequest.addRow(new Object[]{
                    user.getUsername(), rank
                });
            }
            //dong y ket ban
            if (user.getId() == myAccount.getId() && friend.getPerformative() == Friend.ACCEPT) {
                
                listFriendRequest.remove(j);
                myControl.sendData(new ObjectWrapper(ObjectWrapper.REMOVE_REQUEST_FRIEND, listFriendRequest));
                JOptionPane.showMessageDialog(this, "You and " + friend.getUsername() + " have become friend ^^");
                
                boolean isExist = false;
                for (int i = 0; i < myAccount.getListFriend().size(); i++) {
                    Friend f = myAccount.getListFriend().get(i);
                    if (f.getId() == friend.getId()) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    myAccount.getListFriend().add(friend);
                    fillFriend();
                }
            }
        }
        modelFriendRequest.fireTableDataChanged();

        // từ chối kb
    }
    
    public void updateStatus() {
        fillRanking();
        fillFriend();
        fillGroupJoined();
        fillRequestFriend();
    }
    
    private void setProperties() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        labName.setText(myAccount.getName());
        labUsername.setText(myAccount.getUsername());
        labRole.setText(myAccount.getRole());
        labBirthday.setText(sdf.format(myAccount.getBirthday()));
        if (myAccount.getRole().equalsIgnoreCase("player")) {
            menuManageGame.hide();
        }
    }
    
    public void receiveDataProcessing(ObjectWrapper ow) {
        switch (ow.getPerformative()) {
            case ObjectWrapper.REPLY_RANKING:
                setListRankings((ArrayList<Ranking>) ow.getData());
                
                updateStatus();
                break;
            case ObjectWrapper.REPLY_FRIEND_OF_USER:
                myAccount.setListFriend((ArrayList<Friend>) ow.getData());
                fillFriend();
                break;
            case ObjectWrapper.REPLY_GROUP_JOINED:
                setListGroupsJoined((ArrayList<Group>) ow.getData());
                fillGroupJoined();
                
                break;
            case ObjectWrapper.SERVER_INFORM_LIST_REQUEST_FRIEND:
                setListFriendRequest((ArrayList<User>) ow.getData());
                fillRequestFriend();
                break;
            case ObjectWrapper.REPLY_GET_LIST_REQUEST_FRIEND:
                listFriendRequest = (ArrayList<User>) ow.getData();
                fillRequestFriend();
                break;            
            case ObjectWrapper.REPLY_ACCEPT_REQUEST_FRIEND:
                if (ow.getData() instanceof User) {
                    
                    User newFU = (User) ow.getData();
                    Friend newFriend = new Friend();
                    newFriend.setId(newFU.getId());
                    newFriend.setUsername(newFU.getUsername());
                    newFriend.setName(newFU.getName());
                    newFriend.setBirthday(newFU.getBirthday());
                    newFriend.setRole(newFU.getRole());
                    newFriend.setEmail(newFU.getEmail());
                    
                    myAccount.getListFriend().add(newFriend);
                    fillFriend();
                    JOptionPane.showMessageDialog(this, "You and " + newFriend.getUsername() + " have become friend ^^");
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Error");
                }
                break;
            case ObjectWrapper.REPLY_REMOVE_REQUEST_FRIEND:
                
                break;
            case ObjectWrapper.REPLY_REJECT_REQUEST_FRIEND:
                break;
            case ObjectWrapper.REPLY_UNFRIEND:
                if (ow.getData() instanceof Friend) {
                    Friend deleteF = (Friend) ow.getData();
                    for (Friend friend : myAccount.getListFriend()) {
                        if (friend.getId() == deleteF.getId()) {
                            myAccount.getListFriend().remove(friend);
                            break;
                        }
                    }
                    fillFriend();
                } else {
                    JOptionPane.showMessageDialog(this, "Fail when unfriend");
                }
                
                break;
            case ObjectWrapper.SERVER_INFORM_UNFRIEND:
                myControl.sendData(new ObjectWrapper(ObjectWrapper.FRIEND_OF_USER, myAccount));
                break;
            
            case ObjectWrapper.FREE_WAITING:
                //KHONG LAM GI CA
                break;
            case ObjectWrapper.SERVER_SEND_CHALLENGE_COMMUNICATE:// phia nguoi nhan loi thach dau
                
                ReceiveChallengeFrm rcf = new ReceiveChallengeFrm(myControl, myAccount, (Match) ow.getData());
                
                break;
            case ObjectWrapper.SERVER_SEND_ACCEPT_CHALLENGE_COMMUNICATE:// phia nguoi gui loi thach dau

//                myControl.sendData(new ObjectWrapper(ObjectWrapper.CHANGE_STATUS, User.PLAYING));
//                Match match1 = (Match) ow.getData();
                new GameForm(myControl, ((Match) ow.getData()), myAccount).setVisible(true);
                
                this.setVisible(false);
                break;
            case ObjectWrapper.SERVER_INFORM_RESULT_MATCH:
//                Match m = (Match) ow.getData();
                if (((Match) ow.getData()).getListResult().get(0).getOutcome() == 1) {
                    new ResultMatchFrm(((Match) ow.getData()).getListResult().get(0), ((Match) ow.getData()).getListResult().get(1)).setVisible(true);
                    
                } else {
                    new ResultMatchFrm(((Match) ow.getData()).getListResult().get(1), ((Match) ow.getData()).getListResult().get(0)).setVisible(true);
                    
                }
                if (((Match) ow.getData()).getGroup() == null) {
                    this.setVisible(true);
                } else {
                    initPlayInGroup(((Match) ow.getData()).getGroup());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_RANK_OF_GROUP, ow.getData()));
                    
                }
                
                break;
            case ObjectWrapper.SERVER_INFORM_ALL_UPDATE_RANK:
                myControl.sendData(new ObjectWrapper(ObjectWrapper.RANKING, "winrate"));
                break;
            
            case ObjectWrapper.REPLY_KICK_OUT_GROUP:
                myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_MEMBER_OF_GROUP, ow.getData()));
                
                break;
            case ObjectWrapper.REPLY_LEAVE_GROUP:
                myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_MEMBER_OF_GROUP, ow.getData()));
                
                break;
            case ObjectWrapper.SERVER_INFORM_UPDATE_GROUP:
//                 neu nhom dang xuat hien bi tac dong thi se cap nhat lai table
                if (playInGroupFrm == null) {
                    return;
                }
                if (playInGroupFrm.getMyGroup().getId() == ((Group) ow.getData()).getId()
                        && playInGroupFrm.isVisible()) {
                    playInGroupFrm.initTable();
                }
                System.out.println("udate group");
                break;
            case ObjectWrapper.SERVER_INFORM_MESSAGE_OF_GROUP:
                
                if (playInGroupFrm == null) {
                    return;
                }
                if (playInGroupFrm.getMyGroup().getId() == ((Group) ow.getData()).getId()
                        && playInGroupFrm.isVisible()) {
                    playInGroupFrm.updateBoard(((Group) ow.getData()).getMessage());
                }
                
                break;
            case ObjectWrapper.REPLY_GET_MESSAGE_OF_GROUP:
                if (playInGroupFrm == null) {
                    return;
                }
                if (playInGroupFrm.isVisible()) {
                    playInGroupFrm.updateBoard((String) ow.getData());
                }
                
                break;
            case ObjectWrapper.REPLY_JOIN_GROUP_BY_INVITATION:
                if (ow.getData() instanceof Group) {
                    
                    JOptionPane.showMessageDialog(this, "Join Successfully");
                    
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_GROUP_TO_ALL_CLIENT, "UpdateToAll"));
                    // update thanh vien neu dang o trong nhom
                    myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_MEMBER_OF_GROUP, ow.getData()));
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Fail when join group", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case ObjectWrapper.FRIEND_SEND_MESSAGE:
                User receiveU = (User) ow.getData();
                ChatPrivateFrm cpf = new ChatPrivateFrm(myAccount, myControl, ((Friend) receiveU.getListFriend().get(0)));
                cpf.receiveMessage();
                cpf.setVisible(true);
                break;
//            case ObjectWrapper.REPLY_TEST_SEND_TETRISBLOCK:
//                if (ow.getData().equals("ok")) {
//                    JOptionPane.showMessageDialog(this, "Successfully");
//                }
//                break;
        }
    }

    public void leveaGroup(Group group) {
        for (int i = 0; i < listGroupsJoined.size(); i++) {
            Group g = listGroupsJoined.get(i);
            if (g.getId() == group.getId()) {
                listGroupsJoined.remove(i);
                
                break;
            }
        }
        fillGroupJoined();
    }

    public ArrayList<User> getListUsersOnline() {
        return listUsersOnline;
    }

    public void setListUsersOnline(ArrayList<User> listUsersOnline) {
        this.listUsersOnline = listUsersOnline;

        // kiem tra xem co dang choi trong group hay k?
        if (playInGroupFrm != null) {
            playInGroupFrm.setListUsersOnline(listUsersOnline);
        }
    }
    
    public ArrayList<Ranking> getListRankings() {
        return listRankings;
    }
    
    public void setListRankings(ArrayList<Ranking> listRankings) {
        this.listRankings = listRankings;
    }
    
    public ArrayList<Group> getListGroupsJoined() {
        return listGroupsJoined;
    }
    
    public void setListGroupsJoined(ArrayList<Group> listGroupsJoined) {
        this.listGroupsJoined = listGroupsJoined;
    }
    
    public ArrayList<User> getListFriendRequest() {
        return listFriendRequest;
    }
    
    public void setListFriendRequest(ArrayList<User> listFriendRequest) {
        this.listFriendRequest = listFriendRequest;
    }
    
    public PlayInGroupFrm getPlayInGroupFrm() {
        return playInGroupFrm;
    }
    
    public void setPlayInGroupFrm(PlayInGroupFrm playInGroupFrm) {
        this.playInGroupFrm = playInGroupFrm;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRanking = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblFriend = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGroupsJoined = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblFriendRequest = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labUsername = new javax.swing.JLabel();
        labName = new javax.swing.JLabel();
        labBirthday = new javax.swing.JLabel();
        labRole = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuJoinGroup = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuCreateGroup = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnuInvitation = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuRankWithOP = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mnuRankTotalWinMatch = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnuScoreInTournament = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnuCreateTournament = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnuJoinTournament = new javax.swing.JMenuItem();
        menuManageGame = new javax.swing.JMenu();
        mnuMode = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        mnuViewReport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Rank"));

        tblRanking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblRanking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRankingMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRanking);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Friend"));

        tblFriend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFriendMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblFriend);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Groups joined"));

        tblGroupsJoined.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGroupsJoined.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroupsJoinedMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGroupsJoined);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Friend request"));

        tblFriendRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFriendRequest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFriendRequestMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblFriendRequest);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setText("Username:");

        jLabel2.setText("Name:");

        jLabel3.setText("Birthday:");

        jLabel4.setText("Role:");

        labUsername.setText("un");

        labName.setText("name");

        labBirthday.setText("birthday");

        labRole.setText("role");

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/icon/group.png"))); // NOI18N
        jMenu1.setText("Group");

        mnuJoinGroup.setText("Join Group");
        mnuJoinGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuJoinGroupActionPerformed(evt);
            }
        });
        jMenu1.add(mnuJoinGroup);
        jMenu1.add(jSeparator1);

        mnuCreateGroup.setText("Create Group");
        mnuCreateGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCreateGroupActionPerformed(evt);
            }
        });
        jMenu1.add(mnuCreateGroup);
        jMenu1.add(jSeparator5);

        mnuInvitation.setText("Invitation");
        mnuInvitation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInvitationActionPerformed(evt);
            }
        });
        jMenu1.add(mnuInvitation);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/icon/rank2.png"))); // NOI18N
        jMenu2.setText("Ranking");

        mnuRankWithOP.setText("With Other Player");
        mnuRankWithOP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRankWithOPActionPerformed(evt);
            }
        });
        jMenu2.add(mnuRankWithOP);
        jMenu2.add(jSeparator2);

        mnuRankTotalWinMatch.setText("Total Win Match");
        mnuRankTotalWinMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRankTotalWinMatchActionPerformed(evt);
            }
        });
        jMenu2.add(mnuRankTotalWinMatch);
        jMenu2.add(jSeparator3);

        mnuScoreInTournament.setText("Score In Tournament");
        mnuScoreInTournament.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuScoreInTournamentActionPerformed(evt);
            }
        });
        jMenu2.add(mnuScoreInTournament);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/icon/tournament2.png"))); // NOI18N
        jMenu3.setText("Tournament");

        mnuCreateTournament.setText("Manager");
        mnuCreateTournament.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCreateTournamentActionPerformed(evt);
            }
        });
        jMenu3.add(mnuCreateTournament);
        jMenu3.add(jSeparator4);

        mnuJoinTournament.setText("Join");
        mnuJoinTournament.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuJoinTournamentActionPerformed(evt);
            }
        });
        jMenu3.add(mnuJoinTournament);

        jMenuBar1.add(jMenu3);

        menuManageGame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/icon/project-management.png"))); // NOI18N
        menuManageGame.setText("Manage Game");

        mnuMode.setText("Manage Mode");
        mnuMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuModeActionPerformed(evt);
            }
        });
        menuManageGame.add(mnuMode);
        menuManageGame.add(jSeparator6);

        mnuViewReport.setText("View Report");
        menuManageGame.add(mnuViewReport);

        jMenuBar1.add(menuManageGame);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 408, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(39, 39, 39)
                            .addComponent(labName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(labUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(labRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(labUsername)
                    .addComponent(labBirthday))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(labName)
                    .addComponent(labRole))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (myControl != null) {
            
            myControl.closeConnnection();
        }
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void mnuJoinGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuJoinGroupActionPerformed
        ObjectWrapper fun = null;
        for (ObjectWrapper item : myControl.getMyFuntion()) {
            if (item.getData() instanceof JoinGroupFrm) {
                fun = item;
            }
        }
        if (fun != null) {
            myControl.getMyFuntion().remove(fun);
        }
        
        JoinGroupFrm jgf = new JoinGroupFrm(myControl, myAccount);
        
    }//GEN-LAST:event_mnuJoinGroupActionPerformed

    private void mnuCreateGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCreateGroupActionPerformed
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if (activeFun.getData() instanceof CreateGroupFrm) {
                ((CreateGroupFrm) activeFun.getData()).setVisible(true);
                return;
            }
        }
        CreateGroupFrm createGF = new CreateGroupFrm(myControl, myAccount);
        
    }//GEN-LAST:event_mnuCreateGroupActionPerformed

    private void tblRankingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRankingMouseClicked
        int selected = tblRanking.getSelectedRow();
        if (selected >= 0) {
            String username = (String) modelRanking.getValueAt(selected, 1);
            for (Ranking rank : listRankings) {
                if (rank.getUsername().equals(username)) {
                    
                    for (ObjectWrapper ow : myControl.getMyFuntion()) {
                        if (ow.getData() instanceof PlayerDetailsFrm) {
                            PlayerDetailsFrm pdf = (PlayerDetailsFrm) ow.getData();
                            pdf.setMyAccount(myAccount);
                            pdf.setMyControl(myControl);
                            pdf.setListFriendRequest(listFriendRequest);
                            pdf.setRank(rank);
                            pdf.setListUsersOnline(listUsersOnline);
                            pdf.setProperties();
                            pdf.setVisible(true);
                            return;
                        }
                    }
                    PlayerDetailsFrm pdf = new PlayerDetailsFrm(myControl, rank, myAccount, listFriendRequest, listUsersOnline);
                    pdf.setVisible(true);
                    break;
                }
            }
            
        }
        
    }//GEN-LAST:event_tblRankingMouseClicked

    private void tblFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFriendMouseClicked
        int selected = tblFriend.getSelectedRow();
        if (selected >= 0) {
            String username = (String) modelFriend.getValueAt(selected, 0);
            for (Friend friend : myAccount.getListFriend()) {
                if (friend.getUsername().equals(username)) {
                    
                    for (ObjectWrapper ow : myControl.getMyFuntion()) {
                        if (ow.getData() instanceof PlayerDetailsFrm) {
                            PlayerDetailsFrm pdf = (PlayerDetailsFrm) ow.getData();
                            pdf.setMyAccount(myAccount);
                            pdf.setMyControl(myControl);
                            pdf.setListFriendRequest(listFriendRequest);
                            pdf.setFriend(friend);
                            pdf.setListUsersOnline(listUsersOnline);
                            pdf.setPropertiesForFriend();
                            pdf.setVisible(true);
                            return;
                        }
                    }
                    PlayerDetailsFrm pdf = new PlayerDetailsFrm(myControl, friend, myAccount, listFriendRequest, listUsersOnline);
                    
                    break;
                }
            }
            
        }
    }//GEN-LAST:event_tblFriendMouseClicked

    private void tblFriendRequestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFriendRequestMouseClicked
        int selected = tblFriendRequest.getSelectedRow();
        if (selected >= 0) {
            String username = (String) modelFriendRequest.getValueAt(selected, 0);
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to be friend with " + username + " ?", "Reply request", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (choice == JOptionPane.YES_OPTION) {
                // thay doi trang thai loi yeu cau kb
                for (int i = 0; i < listFriendRequest.size(); i++) {
                    User user = listFriendRequest.get(i);
                    
                    Friend friend = user.getListFriend().get(0);
                    if (user.getUsername().equals(username) && friend.getId() == myAccount.getId()) {
                        listFriendRequest.get(i).getListFriend().get(0).setPerformative(Friend.ACCEPT);
                        user.getListFriend().get(0).setPerformative(Friend.ACCEPT);
                        
                        myControl.sendData(new ObjectWrapper(ObjectWrapper.ACCEPT_REQUEST_FRIEND, user));
                        myControl.sendData(new ObjectWrapper(ObjectWrapper.UPDATE_LIST_FRIEND_REQUEST, user));
                        break;
                    }
                }
                
            } else if (choice == JOptionPane.NO_OPTION) {
                for (User user : listFriendRequest) {
                    Friend friend = user.getListFriend().get(0);
                    if (user.getUsername().equals(username) && friend.getId() == myAccount.getId()) {
//                        user.getListFriend().get(0).setPerformative(Friend.REJEST);
                        listFriendRequest.remove(user);
                        fillRequestFriend();
                        myControl.sendData(new ObjectWrapper(ObjectWrapper.REJECT_REQUEST_FRIEND, listFriendRequest));
                        break;
                    }
                }
            }
            
        }
    }//GEN-LAST:event_tblFriendRequestMouseClicked

    private void mnuCreateTournamentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCreateTournamentActionPerformed
        if (myAccount.getRole().equalsIgnoreCase("player")) {
            return;
        }
        ManagerTournamentFrm mtf = new ManagerTournamentFrm(myControl, myAccount);
        
    }//GEN-LAST:event_mnuCreateTournamentActionPerformed

    private void mnuJoinTournamentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuJoinTournamentActionPerformed
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if (activeFun.getData() instanceof JoinTournamentFrm) {
                ((JoinTournamentFrm) activeFun.getData()).setVisible(true);
                return;
            }
        }
        JoinTournamentFrm joinTournamentFrm = new JoinTournamentFrm(myControl, myAccount);
    }//GEN-LAST:event_mnuJoinTournamentActionPerformed

    private void mnuRankWithOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRankWithOPActionPerformed
        
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if (activeFun.getData() instanceof RankingWOPlayerFrm) {
                RankingWOPlayerFrm rf = ((RankingWOPlayerFrm) activeFun.getData());
                
                rf.initTable();
                rf.setVisible(true);
                return;
            }
        }
        RankingWOPlayerFrm rankingFrm = new RankingWOPlayerFrm(myAccount, myControl);
    }//GEN-LAST:event_mnuRankWithOPActionPerformed

    private void mnuRankTotalWinMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRankTotalWinMatchActionPerformed
        for (ObjectWrapper activeFun : myControl.getMyFuntion()) {
            if (activeFun.getData() instanceof RankingBTWinMatchFrm) {
                RankingBTWinMatchFrm rf = ((RankingBTWinMatchFrm) activeFun.getData());
                
                rf.initTable();
                rf.setVisible(true);
                return;
            }
        }
        RankingBTWinMatchFrm rankingFrm = new RankingBTWinMatchFrm(myAccount, myControl);
    }//GEN-LAST:event_mnuRankTotalWinMatchActionPerformed

    private void mnuScoreInTournamentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuScoreInTournamentActionPerformed
        
    }//GEN-LAST:event_mnuScoreInTournamentActionPerformed

    private void tblGroupsJoinedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupsJoinedMouseClicked
        int selected = tblGroupsJoined.getSelectedRow();
        
        if (selected >= 0) {
            String nameGroup = (String) modelGroupsJoined.getValueAt(selected, 1);
            Group myGroup = new Group();
            for (Group group : listGroupsJoined) {
                if (group.getName().equalsIgnoreCase(nameGroup)) {
                    
                    myGroup.setId(group.getId());
                    myGroup.setName(group.getName());
                    break;
                }
            }
            
            initPlayInGroup(myGroup);
        }
    }//GEN-LAST:event_tblGroupsJoinedMouseClicked

    private void mnuInvitationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuInvitationActionPerformed
        ObjectWrapper fun = null;
        for (ObjectWrapper item : myControl.getMyFuntion()) {
            if (item.getData() instanceof InvitationFrm) {
                fun = item;
                break;
            }
        }
        if (fun != null) {
            myControl.getMyFuntion().remove(fun);
        }
        
        (new InvitationFrm(myControl, myAccount)).setVisible(true);
    }//GEN-LAST:event_mnuInvitationActionPerformed

    private void mnuModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuModeActionPerformed
        ObjectWrapper fun = null;
        for (ObjectWrapper ow : myControl.getMyFuntion()) {
            if (ow.getData() instanceof ManageModeFrm) {
                fun = ow;
                break;
            }
        }
        if (fun != null) {
            myControl.getMyFuntion().remove(fun);
        }
        
        ManageModeFrm modeFrm = new ManageModeFrm(myControl, myAccount);
        modeFrm.setVisible(true);
    }//GEN-LAST:event_mnuModeActionPerformed
    
    public void initPlayInGroup(Group myGroup) {
        // neu chua hien lan nao se khoi tao
        if (playInGroupFrm == null) {
            playInGroupFrm = new PlayInGroupFrm(myControl, myAccount, myGroup);
            this.setVisible(false);
        } else {
            playInGroupFrm.setMyGroup(myGroup);
            playInGroupFrm.initTable();
            playInGroupFrm.setVisible(true);
            
            this.setVisible(false);
            
        }
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JLabel labBirthday;
    private javax.swing.JLabel labName;
    private javax.swing.JLabel labRole;
    private javax.swing.JLabel labUsername;
    private javax.swing.JMenu menuManageGame;
    private javax.swing.JMenuItem mnuCreateGroup;
    private javax.swing.JMenuItem mnuCreateTournament;
    private javax.swing.JMenuItem mnuInvitation;
    private javax.swing.JMenuItem mnuJoinGroup;
    private javax.swing.JMenuItem mnuJoinTournament;
    private javax.swing.JMenuItem mnuMode;
    private javax.swing.JMenuItem mnuRankTotalWinMatch;
    private javax.swing.JMenuItem mnuRankWithOP;
    private javax.swing.JMenuItem mnuScoreInTournament;
    private javax.swing.JMenuItem mnuViewReport;
    private javax.swing.JTable tblFriend;
    private javax.swing.JTable tblFriendRequest;
    private javax.swing.JTable tblGroupsJoined;
    private javax.swing.JTable tblRanking;
    // End of variables declaration//GEN-END:variables

}
