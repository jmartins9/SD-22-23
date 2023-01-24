import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class FramedConnection implements AutoCloseable {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final ReentrantLock receiveLock = new ReentrantLock();
    private final DataOutputStream outputStream;
    private final ReentrantLock sendLock = new ReentrantLock();

    public FramedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void send(byte[] data) throws IOException {
        try {
            this.sendLock.lock();
            this.outputStream.writeInt(data.length);
            this.outputStream.write(data);
            this.outputStream.flush();
        } finally {
            this.sendLock.unlock();
        }
    }

    public byte[] receive() throws IOException {
        byte[] data;

        try {
            this.receiveLock.lock();
            int dataLength = this.inputStream.readInt();
            data = new byte[dataLength];
            this.inputStream.readFully(data);
        } finally {
            this.receiveLock.unlock();
        }

        return data;
    }

    public void close() throws IOException {
        this.socket.close();
    }
}