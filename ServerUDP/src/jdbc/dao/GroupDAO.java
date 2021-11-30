package jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Group;
import model.User;

/**
 *
 * @author DatIT
 */
public class GroupDAO extends DAO{

    public GroupDAO() {
        super();
    }
    public boolean createGroup(Group group){
        boolean result = false;
        try {
            String sql = "INSERT INTO tblgroup( name, minimumBagde)\n" +
"                           VALUES( ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, group.getName());
            ps.setInt(2, group.getMinimumBadge());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public ArrayList<Group> getGroupJoined(User user){
        ArrayList<Group> result = new ArrayList<>();
        try {
            String sql = "SELECT b.id, b.name, bangSL.soluong\n" +
"                         FROM tbljoining as a, tblgroup as b, (SELECT c.tblGroupID as i,COUNT(c.IDuser) as soluong\n" +
"									FROM tbljoining as c\n" +
"									GROUP BY c.tblGroupID) as bangSL\n" +
"                           WHERE a.IDuser = ?\n" +
"                           AND bangSL.i = a.tblGroupID\n" +
"                           AND b.id = a.tblGroupID;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setNumMember(rs.getInt("soluong"));
                result.add(group);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public ArrayList<Group> getGroupNotJoined(User user){
        ArrayList<Group> result = new ArrayList<>();
        try {
            String sql = "SELECT a.id,a.name, COUNT(b.IDuser) as soLuong" +
"                         FROM tblgroup as a, tbljoining as b " +
"                           WHERE b.tblGroupID = a.id " +
"                           AND a.id NOT IN (SELECT tblgroup.id  " +
"						FROM tblgroup, tbljoining " +
"                                               WHERE tbljoining.IDuser = ? " +
"                                               AND tbljoining.tblGroupID = tblgroup.id) " +
"                           GROUP BY a.id; " +
"                                    ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setNumMember(rs.getInt("soLuong"));
                result.add(group);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
