package jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Group;
import model.Joining;
import model.User;

/**
 *
 * @author DatIT
 */
public class JoiningDAO extends DAO{

    public JoiningDAO() {
        super();
    }
    public boolean joinGroupAdmin( Group group){
        boolean result = false;
        
        
        String sql = "INSERT INTO tbljoining( roleInGroup, IDuser, tblGroupID)\n" +
"                     VALUES( ?, ?, (SELECT tblgroup.id\n" +
"					FROM tblgroup\n" +
"					WHERE tblgroup.name = ?) );";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, group.getListJoining().get(0).getRoleInGroup());
            ps.setInt(2, group.getListJoining().get(0).getUser().getId());
            ps.setString(3, group.getName());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public boolean joinGroup( Group group){
        boolean result = false;
        
        
        String sql = "INSERT INTO tbljoining( roleInGroup, IDuser, tblGroupID)\n" +
"                     VALUES( ?, ?, ? );";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, group.getListJoining().get(0).getRoleInGroup());
            ps.setInt(2, group.getListJoining().get(0).getUser().getId());
            ps.setInt(3, group.getId());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public ArrayList<Joining> getPlayerInGroup(Group group){
        ArrayList<Joining> result = new ArrayList<>();
        String sql = "SELECT b.id, c.id, c.username, c.name,(\n" +
"                       CASE \n" +
"                               WHEN c.id IN( SELECT tbluser.id\n" +
"					FROM tblresult, tblmatch, tbluser\n" +
"                                       WHERE tblmatch.IDtblGroup = a.id\n" +
"                                       AND tblresult.tblMatchID = tblmatch.id\n" +
"                                       AND tblresult.tblUserID = tbluser.id) THEN (\n" +
"                                               SELECT SUM(tblresult.score)\n" +
"						FROM tblresult, tblmatch\n" +
"						WHERE tblmatch.IDtblGroup = a.id\n" +
"						AND tblresult.tblMatchID = tblmatch.id\n" +
"						AND tblresult.tblUserID = c.id\n" +
"						GROUP BY( tblresult.tblUserID)\n" +
"                                   )\n" +
"                               ELSE 0\n" +
"                       END\n" +
")                      as tongDiem, b.roleInGroup\n" +
"                       FROM tblgroup as a, tbljoining as b, tbluser as c\n" +
"                       WHERE a.id = ?\n" +
"                       AND c.id = b.IDuser\n" +
"                       AND a.id = b.tblGroupID\n" +
"                       ORDER BY tongDiem DESC;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, group.getId());
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Joining joining = new Joining();
                joining.setId(rs.getInt(1));
                joining.setScore(rs.getInt(5));
                joining.setRoleInGroup(rs.getString(6));
                
                User user = new User();
                user.setId(rs.getInt(2));
                user.setUsername(rs.getString(3));
                user.setName(rs.getString(4));
                
                joining.setUser(user);
                result.add(joining);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public boolean leaveGroup(Joining joining){
        boolean result = false;
        
        
        String sql = "DELETE FROM tbljoining"
                + "   WHERE ID = ?;";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, joining.getId());
            
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
