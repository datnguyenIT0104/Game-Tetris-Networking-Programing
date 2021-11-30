package jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Friend;
import model.ObjectWrapper;
import model.User;

/**
 *
 * @author DatIT
 */
public class UserDAO extends DAO{

    public UserDAO() {
        super();
    }
    
    public boolean checkLogin(User user){
        boolean result = false;
        
        SimpleDateFormat sdf  = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String sql = "SELECT a.id as iduser, a.name as nameuser, a.isBanned as banuser, a.email as emailuser, a.birthday as birthdayuser, a.role as roleuser\n" +
                        "FROM tbluser as a\n" +
                        "WHERE ? = a.username\n" +
                        "AND ? = a.password;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if( rs.next()){
                user.setId(rs.getInt("iduser"));
                user.setRole(rs.getString("roleuser"));
                user.setName(rs.getString("nameuser"));
                user.setEmail(rs.getString("emailuser"));
                user.setBirthday( rs.getDate("birthdayuser"));
                user.setStatus( User.ONLINE);
                user.setIsBanned( ( rs.getInt("banuser") == 1 ? true : false));
               
                result = true;
            }
            
//            if( !user.isIsBanned()) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean createAccount( User u){
        boolean result = false;
        
        
        String sql = "INSERT INTO tbluser(username, password, email, name, birthday, role)"
                + "   VALUES(?, ?, ?, ?, ?, ?);";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getName());
            ps.setString(5, sdf.format(u.getBirthday()));
            ps.setString(6, u.getRole());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Create acc");
        return result;
    }
    public boolean updateAccount( User u){
        boolean result = false;
        String sql = "UPDATE tbluser"
                + "SET  password = ?, email = ?, name = ?, birthday = ?, role = ?, isBanned = ? )"
                + "WHERE id = ?   ;";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, u.getPassword());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getName());
            ps.setString(4, sdf.format(u.getBirthday()));
            ps.setString(5, u.getRole());
            ps.setInt(6, ( u.isIsBanned() ? 1: 0));
//            ps.setInt(7, ( u.isStatus() ? 1: 0));           // status ở đây là trang thái online hay offline
            ps.setInt(7, u.getId());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    
}
