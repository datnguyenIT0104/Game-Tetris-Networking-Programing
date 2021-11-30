package jdbc.dao;

import model.Tournament;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static jdbc.dao.DAO.con;
import model.TournamentUser;
import model.User;
/**
 *
 * @author DatIT
 */
public class TournamentUserDAO extends DAO{

    public TournamentUserDAO() {
        super();
    }
    public boolean joinTournament(Tournament t){
        boolean result = false;
        String sql = "INSERT INTO tbltournamentuser( idTblUser, idTblTournament)\n" +
"                     VALUES( ?, (SELECT a.id\n" +
"                                   FROM tbltournament as a\n" +
"                                   WHERE a.name = ?));";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, t.getListTournamentUsers().get(0).getUser().getId());
            ps.setString(2, t.getName());
            
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public ArrayList<TournamentUser> getPlayerInTournament(Tournament tournament){
        ArrayList<TournamentUser> result = new ArrayList<>();
        
        String sql = "SELECT b.id as idTU, c.id, c.username, c.name,(\n" +
"                           CASE \n" +
"                               WHEN c.id IN( SELECT tbluser.id\n" +
"						FROM tblresult, tblmatch, tbluser\n" +
"                                               WHERE tblmatch.tblTournamentID = a.id\n" +
"                                               AND tblresult.tblMatchID = tblmatch.id\n" +
"                                               AND tblresult.tblUserID = tbluser.id) THEN (\n" +
"                                               SELECT SUM(tblresult.score)\n" +
"						FROM tblresult, tblmatch\n" +
"						WHERE tblmatch.tblTournamentID = a.id\n" +
"						AND tblresult.tblMatchID = tblmatch.id\n" +
"						AND tblresult.tblUserID = c.id\n" +
"						GROUP BY( tblresult.tblUserID)\n" +
"                                               )\n" +
"                               ELSE 0\n" +
"                           END\n" +
"                           ) as tongDiem\n" +
"                   FROM tbltournament as a, tbltournamentuser as b, tbluser as c\n" +
"                   WHERE a.id =  ?\n" +
"                   AND c.id = b.idTblUser\n" +
"                   AND a.id = b.idTblTournament"
                + " ORDER BY tongDiem DESC;";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, tournament.getId());
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                TournamentUser tu = new TournamentUser();
                tu.setId(rs.getInt("idTU"));
                tu.setTotalScore(rs.getInt(5));
                // dong goi user
                User user = new User();
                user.setId(rs.getInt(2));
                user.setUsername(rs.getString(3));
                user.setName(rs.getString(4));
                
                tu.setUser(user);
                result.add(tu);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
