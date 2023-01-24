import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;


public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            EP ep = new EP();

            while (true) {
                Socket socket = ss.accept();

                Thread conn = new Thread(new ClientConnection(socket,ep));
                conn.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
