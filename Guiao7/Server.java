import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.asList;

class ContactManager {
    private ReentrantLock rl = new ReentrantLock();
    private HashMap<String, Contact> contacts;

    public ContactManager() {
        contacts = new HashMap<>();
        // example pre-population
        this.update(new Contact("John", 20, 253123321, null, asList("john@mail.com")));
        this.update(new Contact("Alice", 30, 253987654, "CompanyInc.", asList("alice.personal@mail.com", "alice.business@mail.com")));
        this.update(new Contact("Bob", 40, 253123456, "Comp.Ld", asList("bob@mail.com", "bob.work@mail.com")));
    }


    // @TODO
    public void update(Contact c) {
        this.rl.lock();
        this.contacts.put(c.name(),c);
        this.rl.unlock();
    }

    // @TODO
    public ContactList getContacts() {
        try {
            this.rl.lock();
            ContactList cl = new ContactList();
            cl.addAll(contacts.values().stream().map(Contact::clone).toList());
            return cl;
        } finally {
            this.rl.unlock();
        }
    }

}

class ServerWorker implements Runnable {
    private Socket socket;
    private ContactManager manager;

    public ServerWorker (Socket socket, ContactManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    // @TODO
    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());

            try{
                manager.update(Contact.deserialize(in));
                System.out.println(manager.getContacts().toString());
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Connection closed");
            }

        } catch (IOException e) {
            System.out.println("Erro a abrir conexão de input");
        }

    }
}



public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        ContactManager manager = new ContactManager();

        while (true) {
            System.out.println(manager.getContacts().toString());
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, manager));
            worker.start();
        }
    }

}
