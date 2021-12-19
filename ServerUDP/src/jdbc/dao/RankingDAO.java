package jdbc.dao;

import java.util.ArrayList;
import model.Ranking;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Tournament;
import model.User;
/**
 *
 * @author DatIT
 */
public class RankingDAO extends DAO{

    public RankingDAO() {
        super();
    }
    public ArrayList<Ranking> getRankingByWinRate(){
        ArrayList<Ranking> result = new ArrayList<>();
//        String sql = "SELECT a.id, a.username, a.name, a.role, a.email, a.birthday, a.isBanned," +
//"                     (      (SELECT COUNT(b.outcome)   " +
//"                               FROM tblresult as b  " +
//"                               WHERE b.tblUserID = a.id " +
//"                               AND b.outcome = 1" +
//"                               GROUP BY b.tblUserID)/ " +
//"                               (SELECT COUNT(c.tblUserID) " +
//"                               FROM tblresult as c\n" +
//"                               WHERE c.tblUserID = a.id)*100 " +
//"                      ) as tilethang " +
//"                       FROM tbluser as a " +
//"                       ORDER BY tilethang DESC;";
//        try {
//            PreparedStatement ps = con.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while( rs.next()){
//                Ranking r = new Ranking();
//                r.setId( rs.getInt(1));
//                r.setUsername(rs.getString(2));
//                r.setName(rs.getString(3));
//                r.setRole(rs.getString(4));
//                r.setEmail(rs.getString(5));
//                r.setBirthday(rs.getDate(6));
//                r.setIsBanned( ( rs.getInt(7) == 1 ? true : false));
//                r.setWinRate( rs.getFloat("tilethang"));
//                result.add(r);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date now = new Date();
        String sql = "{call xep_hang_ti_le_thang(?)}";
        
        try {
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, sdf.format(now));
            ResultSet rs = cs.executeQuery();
            while( rs.next()){
                Ranking r = new Ranking();
                r.setId( rs.getInt(1));
                r.setUsername(rs.getString(2));
                r.setName(rs.getString(3));
                r.setRole(rs.getString(4));
                r.setEmail(rs.getString(5));
                r.setBirthday(rs.getDate(6));
                r.setIsBanned( ( rs.getInt(7) == 1 ? true : false));
                r.setWinRate( rs.getFloat("tilethang"));
                r.setBadge(rs.getInt("soHuyHieu"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public ArrayList<Ranking> getRankingByRateWinWOPlayer(User u){
        ArrayList<Ranking> result = new ArrayList<>();
        String sql = "SELECT a.id, a.username, a.name, a.role,\n" +
"	(               (SELECT COUNT(c.outcome)\n" +
"                           FROM tblresult as b, tblresult as c\n" +
"                           WHERE b.tblUserID = a.id\n" +
"                           AND b.tblMatchID = c.tblMatchID\n" +
"                           AND c.tblUserID = ?\n" +
"                           AND c.outcome = 1\n" +
"                           GROUP BY c.tblUserID)/\n" +
"                           (SELECT COUNT(c.tblUserID)\n" +
"                           FROM tblresult as b, tblresult as c\n" +
"                           WHERE b.tblUserID = a.id\n" +
"                           AND b.tblMatchID = c.tblMatchID\n" +
"                           AND c.tblUserID = ?\n" +
"                           GROUP BY c.tblUserID\n" +
"                           )*100\n" +
"                           ) as tilethang, (SELECT COUNT(c.tblUserID)\n" +
"                                           FROM tblresult as b, tblresult as c\n" +
"                                           WHERE b.tblUserID = a.id\n" +
"                                           AND b.tblMatchID = c.tblMatchID\n" +
"                                           AND c.tblUserID =  ?\n" +
"                                           GROUP BY c.tblUserID) as sotran" +
"                   FROM tbluser as a\n" +
"                   WHERE a.id <> ?\n" +
"                   AND (SELECT COUNT(c.tblUserID)\n" +
"                           FROM tblresult as b, tblresult as c\n" +
"                           WHERE b.tblUserID = a.id\n" +
"                           AND b.tblMatchID = c.tblMatchID\n" +
"                           AND c.tblUserID = ?\n" +
"                           GROUP BY c.tblUserID) <> 0\n" +
"                           ORDER BY tilethang DESC;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, u.getId());
            ps.setInt(2, u.getId());
            ps.setInt(3, u.getId());
            ps.setInt(4, u.getId());
            ps.setInt(5, u.getId());
            
            ResultSet rs = ps.executeQuery();
            while( rs.next()){
                Ranking r = new Ranking();
                r.setId( rs.getInt(1));
                r.setUsername(rs.getString(2));
                r.setName(rs.getString(3));
                r.setRole(rs.getString(4));
                r.setWinRateWOPlayer( rs.getFloat("tilethang") );
                r.setTotalMatch(rs.getInt("sotran"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public ArrayList<Ranking> getRankingByTotalWinMatch(){
        ArrayList<Ranking> result = new ArrayList<>();
        String sql = "SELECT a.id, a.username, a.name, a.role, " +
"                           (SELECT COUNT(b.outcome) " +
"                           FROM tblresult as b " +
"                           WHERE b.tblUserID = a.id " +
"                           AND b.outcome = 1 " +
"                           GROUP BY b.tblUserID " +
"                           ) as tongtranthang " +
"                           FROM tbluser as a " +
"                           ORDER BY tongtranthang DESC;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            while( rs.next()){
                Ranking r = new Ranking();
                r.setId( rs.getInt(1));
                r.setUsername(rs.getString(2));
                r.setName(rs.getString(3));
                r.setRole(rs.getString(4));
                r.setTotalWinMatch(rs.getInt("tongtranthang"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public ArrayList<Ranking> getRankingByScoreInTournament( Tournament t){
        ArrayList<Ranking> result = new ArrayList<>();
        String sql = "SELECT a.id, a.name, a.username, a.role,  SUM(d.score) as tongdiem\n" +                         
"                     FROM tbluser as a, tbltournament as b,  tblmatch as c, tblresult as d\n" +
"                       WHERE b.id = ?\n" +
"                       AND b.id = c.tblTournamentID\n" +
"                       AND c.id = d.tblMatchID\n" +
"                       AND d.tblUserID = a.id\n" +
"                       GROUP BY a.id\n" +
"                       ORDER BY tongdiem DESC;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, t.getId());
            ResultSet rs = ps.executeQuery();
            while( rs.next()){
                Ranking r = new Ranking();
                r.setId( rs.getInt(1));
                r.setUsername(rs.getString(2));
                r.setName(rs.getString(3));
                r.setRole(rs.getString(4));
                r.setScoreInTournament(rs.getInt("tongdiem"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
