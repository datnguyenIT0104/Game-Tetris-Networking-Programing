package jdbc.dao;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import static jdbc.dao.DAO.con;
import model.Match;
import model.Result;

/**
 *
 * @author DatIT
 */
public class ResultDAO extends DAO {

    public ResultDAO() {
        super();
    }

    public boolean addResult(Match match) {
        boolean res = false;

        String sql = "INSERT INTO tblresult( score, outcome, tblMatchID, tblUserID, timealive)\n"
                + "                       VALUES(?, ?, (SELECT id FROM tblmatch WHERE playtime = ?) , ?, ?);";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        
        for (Result result : match.getListResult()) {

            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, result.getScore());
                ps.setInt(2, result.getOutcome());
                ps.setString(3, sdf.format(match.getPlayTime()));
                ps.setInt(4, result.getUser().getId());
                ps.setLong(5, result.getTimeAlive());

                int count = ps.executeUpdate();
                if (count > 0) {
                    res = true;
                }else
                    res = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
