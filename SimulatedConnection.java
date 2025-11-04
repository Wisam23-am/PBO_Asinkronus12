import java.util.Arrays;
import java.util.List;

public class SimulatedConnection implements DBConnectionInterface {
    private boolean isConnected = false;

    @Override
    public void connect(String connectionInfo) {
        System.out.println("SimulatedConnection: Simulasi koneksi untuk development. (Info: " + connectionInfo + ")");
        this.isConnected = true;
        System.out.println("SimulatedConnection: Koneksi simulasi AKTIF.");
    }

    @Override
    public void disconnect() {
        System.out.println("SimulatedConnection: Simulasi koneksi ditutup.");
        this.isConnected = false;
    }

    @Override
    public List<String> executeQuery(String query) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to SimulatedConnection!");
        }
        System.out.println("SimulatedConnection: Menjalankan query simulasi '" + query + "'");
        return Arrays.asList("simulated_wisam@example.com", "simulated_aqila@dev.local");
    }

    @Override
    public boolean isConnected() {
        return this.isConnected;
    }
}