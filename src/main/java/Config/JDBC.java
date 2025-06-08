package Config;
import com.mysql.cj.jdbc.Driver;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
public class JDBC {
    // create connection
    public static Connection getConnection()
    {
        Dotenv dotenv = Dotenv.load();
        Connection c = null;
        try {
            // register MySQL Driver with DriverManager
            DriverManager.registerDriver(new Driver());
            // parameters
            String url = dotenv.get("DB_URL");
            String userName = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");
            // connection
            c = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }
    // close connection
    public static void closeConnection(Connection c)
    {
        try
        {
            if (c != null)
            {
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
