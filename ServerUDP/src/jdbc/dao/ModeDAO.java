package jdbc.dao;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import static jdbc.dao.DAO.con;
import model.Mode;
/**
 *
 * @author DatIT
 */
public class ModeDAO extends DAO{

    public ModeDAO() {
        super();
    }
    public boolean createMode(Mode mode){
        boolean result = false;
        String sql = "INSERT INTO tblMode(name, speed, time)"
                + "VALUES( ?, ?, ?);";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, mode.getName());
            ps.setInt(2, mode.getSpeed());
            ps.setInt(3, mode.getTime());
            
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean updateMode(Mode mode){
        boolean result = false;
        String sql = "UPDATE tblMode"
                + "   SET speed = ?, time = ?"
                + "   WHERE id = ?";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mode.getSpeed());
            ps.setInt(2, mode.getTime());
            ps.setString(3, mode.getName());
            
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public ArrayList<Mode> getAllMode(){
        ArrayList<Mode> result = new ArrayList<>();
        
        String sql = "SELECT * FROM tetris.tblmode;";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while( rs.next()){
                Mode mode = new Mode();
                mode.setId(rs.getInt("id"));
                mode.setName(rs.getString("name"));
                mode.setTime(rs.getInt("time"));
                mode.setSpeed(rs.getInt("speed"));
                result.add(mode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
