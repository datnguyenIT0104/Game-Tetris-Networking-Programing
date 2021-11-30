package jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author DatIT
 */
public class DAO {
    public static Connection con;
    public DAO(){
        if(con == null){
            String dbUrl = 
                "jdbc:mysql://localhost:3306/tetris?autoReconnect=true&useSSL=false";                              // sua dong nay
            String dbClass = "com.mysql.jdbc.Driver";
            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection(dbUrl, "root", "ctcm0104");// sua dong nay
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
