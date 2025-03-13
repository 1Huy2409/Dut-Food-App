package Database;
import com.mysql.cj.jdbc.Driver;

import java.sql.*;
// import java.sql.DriverManager
public class JDBC {
    // create connection
    public static Connection getConnection()
    {
        Connection c = null;
        try {
            // register MySQL Driver with DriverManager
            DriverManager.registerDriver(new Driver());
            // parameters
            String url = "jdbc:mySQL://localhost:3306/demo";
            String userName = "root";
            String password = "qwertyuiop0";
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
                System.out.println("The connection to our Database has been closed!!!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // get information of your connection to database
    public static void printInformation(Connection c)
    {
        try
        {
            if (c != null)
            {
                DatabaseMetaData metaData = c.getMetaData();
                System.out.println(metaData.toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
