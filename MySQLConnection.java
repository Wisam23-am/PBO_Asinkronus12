import java.util.Arrays;
import java.util.List;

public class MySQLConnection implements DBConnectionInterface {
    private boolean isConnected = false;

    @Override
    public void connect(String connectionString) {
        System.out.println("MySQL: Connecting using '" + connectionString + "'");
        this.isConnected = true;
        System.out.println("MySQL: Connection established.");
    }

    @Override
    public void disconnect() {
        System.out.println("MySQL: Connection closed.");
        this.isConnected = false;
    }

    @Override
    public List<String> executeQuery(String query) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to MySQL database!");
        }
        System.out.println("MySQL: Executing query '" + query + "'");
        return Arrays.asList("wisam@example.com", "aqila@example.com");
    }

    @Override
    public boolean isConnected() {
        return this.isConnected;
    }
}