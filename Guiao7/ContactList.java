import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class ContactList extends ArrayList<Contact> {

    // @TODO
    public void serialize (DataOutputStream out) throws IOException {
        out.write(this.size());
        for (Contact c : this) {
            c.serialize(out);
        }
    }

    // @TODO
    public static ContactList deserialize (DataInputStream in) throws IOException {
        int tam = in.read();
        ContactList lc = new ContactList();
        for (int i = 0; i<tam; i++) {
            lc.add(Contact.deserialize(in));
        }
        return lc;
    }

}
