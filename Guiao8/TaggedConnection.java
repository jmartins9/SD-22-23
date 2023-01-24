import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final ReentrantLock receiveLock = new ReentrantLock();
    private final DataOutputStream outputStream;
    private final ReentrantLock sendLock = new ReentrantLock();


    public static class Frame {
        public final int tag;
        public final byte[] data;

        public Frame(int tag, byte[] data) {
            this.tag = tag;
            this.data = data;
        }
    }
    public TaggedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void send(Frame frame) throws IOException {
        this.send(frame.tag,frame.data);
    }

    public void send(int tag, byte[] data) throws IOException {
        try {
            this.sendLock.lock();
            this.outputStream.writeInt(tag);
            this.outputStream.writeInt(data.length);
            this.outputStream.write(data);
            this.outputStream.flush();
        } finally {
            this.sendLock.unlock();
        }
    }

    public Frame receive() throws IOException {
        int tag;
        byte[] data;

        try {
            this.receiveLock.lock();
            tag = this.inputStream.readInt();
            int dataLength = this.inputStream.readInt();
            data = new byte[dataLength];
            this.inputStream.readFully(data);
        } finally {
            this.receiveLock.unlock();
        }

        return new Frame(tag,data);
    }

    public void close() throws IOException {
        this.socket.close();
    }
}
