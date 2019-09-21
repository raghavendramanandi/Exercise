import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaSeH2Memory {

    public static void main(String[] args) {

        String url = "jdbc:h2:mem:";

        try {
                Connection con = DriverManager.getConnection(url);
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery("SELECT 1+1");

            if (rs.next()) {

                System.out.println(rs.getInt(1));
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JavaSeH2Memory.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}