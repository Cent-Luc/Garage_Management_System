import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDbConn {
    private final String url = "jdbc:postgresql://127.0.0.1:5432/garage_management_system";
    private final String user = "postgres";
    private final String password = "Dat36ory:)";

    // Attempt to connect and return a connection object
    public Connection connect() {
	Connection conn = null;
	try {
	    conn = DriverManager.getConnection(url, user, password);
	    System.out.println("Connected to the server successfully.");
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	}

	return conn;
    }
}
