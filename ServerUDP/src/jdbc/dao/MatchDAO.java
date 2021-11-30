package jdbc.dao;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import static jdbc.dao.DAO.con;
import model.Match;

/**
 *
 * @author DatIT
 */
public class MatchDAO extends DAO{

    public MatchDAO() {
        super();
    }
    public boolean addMatch(Match match){
        boolean result = false;
        
        String sql = "INSERT INTO tblmatch(note, tblModeID,  playtime)\n" +
"                       VALUES(?, ?, ?);";
        
        if( match.getTournament() != null){
            sql =  "INSERT INTO tblmatch(note, tblModeID,  playtime, tblTournamentID)\n" +
"                       VALUES(?, ?, ?, ?);";
        }else if( match.getGroup() != null)
            sql = "INSERT INTO tblmatch(note, tblModeID,  playtime, IDtblGroup)\n" +
"                       VALUES(?, ?, ?, ?);";
        
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, match.getNote());
            ps.setInt(2, match.getMode().getId());
            ps.setString(3, sdf.format(match.getPlayTime()) );
            if( match.getTournament() != null){
                ps.setInt(4, match.getTournament().getId());
            }else if( match.getGroup() != null)
                ps.setInt(4, match.getGroup().getId());
            
            int count = ps.executeUpdate();
            if( count > 0) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
