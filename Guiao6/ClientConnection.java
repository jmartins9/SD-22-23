import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection implements Runnable{
    private Socket connection;
    private EP ep;

    public ClientConnection(Socket connection, EP ep) {
        this.connection = connection;
        this.ep = ep;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter out = new PrintWriter(connection.getOutputStream());

            int sum = 0;
            String line;
            while ((line = in.readLine()) != null) {
                int vrec = Integer.parseInt(line);
                sum += vrec;

                ep.updateSumT(vrec);

                out.println(sum);
                out.flush();
            }
            connection.shutdownInput();

            out.println(ep.getMed());
            out.flush();

            connection.shutdownOutput();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
