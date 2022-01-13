import java.sql.*;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Result;

public final class Connect {

    private final String USERNAME = "root"; // change with your MySQL username, the default username is 'root'
    private final String PASSWORD = ""; // change with your MySQL password, the default password is empty
    private final String DATABASE = "gui"; // change with the database name that you use
    private final String HOST = "localhost:3306"; // change with your MySQL host, the default port is 3306
    private final String CONECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private Connection con;
    private Statement st;
    private static Connect connect;

    /**
     * Constructor for Connect class
     * <br>
     * This class is used singleton design pattern, so this class only have one instance
     */
    public Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect the database, the system is terminated!");
            System.exit(0);
        }
    }

    /**
     * This method is used for get instance from Connect class
     * @return Connect This returns instance from Connect class
     */
    public static synchronized Connect getConnection() {
        /**
         * If the connect is null then:
         *   - Create the instance from Connect class
         *   - Otherwise, just assign the previous instance of this class
         */
        return connect = (connect == null) ? new Connect() : connect;
    }

    /**
     * This method is used for SELECT SQL statements.
     * @param //String This is the query statement
     * @return ResultSet This returns result data from the database
     */
    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(query);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * This method is used for INSERT, UPDATE, or DELETE SQL statements.
     * @param //String This is the query statement
     */
    public void executeUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used for SELECT, INSERT, UPDATE, or DELETE SQL statements using prepare statement.
     * @param //String This is the query statement
     */
    public PreparedStatement preparedStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }

    private static Connect instance = null;

    public static Connect getInstance() {
        // TODO Auto-generated method stub
        if(instance == null) instance = new Connect();
        return instance;
    }

    public ResultSet validate(String query) {
        ResultSet rs = null;

        try {
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rs;
    }


}