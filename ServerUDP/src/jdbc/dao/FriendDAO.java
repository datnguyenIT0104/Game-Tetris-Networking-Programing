package jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static jdbc.dao.DAO.con;
import model.Friend;
import model.User;

/**
 *
 * @author DatIT
 */
public class FriendDAO extends DAO{

    public FriendDAO() {
        super();
    }
    
    public ArrayList<Friend> getFriend(User user){
        ArrayList<Friend> result = new ArrayList<>();
        
        String sql = "SELECT friend.*\n" +
                     "FROM tbluser as a, (SELECT d.*, c.id as idu\n" +
"					FROM tbluser as c, tblfriend as b, tbluser d\n" +
"					WHERE b.tbluser1 = c.id\n" +
"					AND b.tbluser2 = d.id\n" +
"					AND c.id <> d.id) as friend\n" +
                    "WHERE a.username = ? \n" +
                    "AND a.password = ?\n" +
                    "AND friend.idu = a.id;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            while( rs.next()){
                Friend friend = new Friend();
                friend.setId( rs.getInt("id"));
                friend.setUsername( rs.getString("username"));
                friend.setName(rs.getString("name"));
                friend.setRole(rs.getString("role"));
                friend.setBirthday( rs.getDate("birthday"));
                friend.setEmail(rs.getString("email"));
                
                // them vao danh sach ban be
                result.add(friend);
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean saveAddFriend( User u){
        boolean result = false;
        String sql = "INSERT INTO tblfriend( tbluser1, tbluser2)"
                + "     VALUES( ?, ?);";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, u.getId());
            ps.setInt(2, u.getListFriend().get(0).getId());
            int count = ps.executeUpdate();
            
            ps.setInt(2, u.getId());
            ps.setInt(1, u.getListFriend().get(0).getId());
            int count2 = ps.executeUpdate();
            
            if( count > 0 && count2 > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public boolean deleteFriend(User u){
        boolean result = false;
        String sql = "DELETE FROM tblfriend"
                + "   WHERE tbluser1 = ?"
                + "   AND tbluser2 = ?;";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, u.getId());
            ps.setInt(2, u.getListFriend().get(0).getId());
            int count = ps.executeUpdate();
            
            ps.setInt(2, u.getId());
            ps.setInt(1, u.getListFriend().get(0).getId());
            int count2 = ps.executeUpdate();
            
            if( count > 0 && count2 > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
