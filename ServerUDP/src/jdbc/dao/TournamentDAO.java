package jdbc.dao;

import java.sql.Date;
import java.text.SimpleDateFormat;
import model.Tournament;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.TournamentUser;
import model.User;
/**
 *
 * @author DatIT
 */
public class TournamentDAO extends DAO{

    public TournamentDAO() {
        super();
    }
    public boolean createTournament(Tournament tournament){
        boolean result = false;
        String sql = "INSERT INTO tbltournament( name, createTime, amount, endDate)"
                + "   VALUES (? , ?, ?, ?);";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tournament.getName());
            ps.setString(2, sdf.format( tournament.getCreateTime()));
            ps.setInt(3, tournament.getAmount());
            ps.setString(4, sdf.format(tournament.getEndDate()));
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public boolean updateTournament(Tournament tournament){
        boolean result = false;
        String sql = "UPDATE tbltournament( name, amount)"
                + "   SET name = ?, amount = ?"
                + "   WHERE id = ?;";
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tournament.getName());
            ps.setInt(2, tournament.getAmount());
            ps.setInt(3, tournament.getId());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public boolean deleteTournament(Tournament tournament){
        boolean result = false;
        String sql = "DELETE "
                + "   FROM tbltournament"
                + "   WHERE id = ?;";
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, tournament.getId());
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public ArrayList<Tournament> getTournaments(java.util.Date date){
        ArrayList<Tournament> result = new ArrayList<>();
        
        String sql = "SELECT a.id as idTo, a.name as nameTo, a.endDate, c.id, c.username, c.name" +
"                     FROM tbltournamentuser as b, tbltournament as a, tbluser as c" +
"                     WHERE b.idTblTournament = a.id" +
"                       AND c.id = b.idTblUser" +
"                       AND ? < a.endDate; ";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sdf.format(date));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                
                boolean isExist = false;
                for (int i = 0; i < result.size(); i++) {
                    Tournament t = result.get(i);
                    if( t.getId() == rs.getInt(1)){
                        isExist = true;
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setName(rs.getString("name"));
                        
                        TournamentUser tu = new TournamentUser();
                        tu.setUser(user);
                        
                        result.get(i).getListTournamentUsers().add(tu);
                        break;
                    }
                }
                if( !isExist){
                    Tournament t = new Tournament();
                    t.setId(rs.getInt("idTO"));
                    t.setName(rs.getString("nameTo"));
                    t.setEndDate( rs.getDate("endDate"));
                    // dong goi thuoc tinh nguoi choi
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setName(rs.getString("name"));
                    
                    TournamentUser tu = new TournamentUser();
                    tu.setUser(user);
                    
                    t.getListTournamentUsers().add(tu);
                    result.add(t);
                    
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    public ArrayList<Tournament> getAllTournaments(){
        ArrayList<Tournament> result = new ArrayList<>();
        
        String sql = "SELECT a.id as idTo, a.name as nameTo, a.endDate, c.id, c.username, c.name" +
"                     FROM tbltournamentuser as b, tbltournament as a, tbluser as c" +
"                     WHERE b.idTblTournament = a.id" +
"                       AND c.id = b.idTblUser;";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                
                boolean isExist = false;
                for (int i = 0; i < result.size(); i++) {
                    Tournament t = result.get(i);
                    if( t.getId() == rs.getInt(1)){
                        isExist = true;
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setName(rs.getString("name"));
                        
                        TournamentUser tu = new TournamentUser();
                        tu.setUser(user);
                        
                        result.get(i).getListTournamentUsers().add(tu);
                        break;
                    }
                }
                if( !isExist){
                    Tournament t = new Tournament();
                    t.setId(rs.getInt("idTO"));
                    t.setName(rs.getString("nameTo"));
                    t.setEndDate( rs.getDate("endDate"));
                    // dong goi thuoc tinh nguoi choi
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setName(rs.getString("name"));
                    
                    TournamentUser tu = new TournamentUser();
                    tu.setUser(user);
                    
                    t.getListTournamentUsers().add(tu);
                    result.add(t);
                    
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
}
