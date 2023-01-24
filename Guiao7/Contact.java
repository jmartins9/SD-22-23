import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
    private String company;     // Pode ser null
    private ArrayList<String> emails;

    public Contact (String name, int age, long phoneNumber, String company, List<String> emails) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.emails = new ArrayList<>(emails);
    }

    public String name() { return name; }
    public int age() { return age; }
    public long phoneNumber() { return phoneNumber; }
    public String company() { return company; }      // boolean + valor
    public List<String> emails() { return new ArrayList(emails); }  // size + values

    // @TODO
    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF(name);
        out.write(age);
        out.writeLong(phoneNumber);

        if (company!=null) {
            out.writeBoolean(true);
            out.writeUTF(company);
        } else {
            out.writeBoolean(false);
        }

        out.write(emails.size());
        for (String email : emails) {
            out.writeUTF(email);
        }
    }

    // @TODO
    public static Contact deserialize (DataInputStream in) throws IOException {
        String name = in.readUTF();
        int age = in.read();
        long phone = in.readLong();
        boolean ce = in.readBoolean();
        String company = null;
        if (ce) company = in.readUTF();

        int tam = in.read();
        List<String> emails = new ArrayList<>(tam);
        for(int i = 0; i<tam; i++) {
            emails.add(in.readUTF());
        }
        return new Contact(name,age,phone,company,emails);
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.age).append(";");
        builder.append(this.phoneNumber).append(";");
        builder.append(this.company).append(";");
        builder.append(this.emails.toString());
        builder.append("}");
        return builder.toString();
    }

    public Contact clone() {
        return new Contact(this.name,this.age,this.phoneNumber,this.company, new ArrayList<>(this.emails));
    }

}
