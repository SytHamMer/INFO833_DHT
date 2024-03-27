import java.util.ArrayList;
import java.util.HashMap;

public class Message {
    private String type;
    private String sousType;
    //id et loc
    private ArrayList<Integer> senders;

    private HashMap<String,String> data;

    // Message sent by node for the first time
    public Message(String type, String sousType, Integer idSender, HashMap<String,String> data) {
        this.type = type;
        this.sousType = sousType;
        this.senders = new ArrayList<>();
        this.senders.add(idSender);
        this.data = data;
    }

    // Message transmitted by a node (multiple senders)
    public Message(String type, String sousType, ArrayList<Integer> senders, HashMap<String,String> data) {
        this.type = type;
        this.sousType = sousType;
        this.senders = senders;
        this.data = data;
    }


    public String getType() {
        return type;
    }

    public String getSousType() {
        return sousType;
    }

    // Add a sender to the list of senders
    public void addSender(Integer idSender) {
        this.senders.add(idSender);
    }

    public ArrayList<Integer> getSenders() {
        return senders;
    }
    public HashMap<String,String> getData() {
        return data;
    }


    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", sousType='" + sousType + '\'' +
                ", senders=" + senders +
                ", data=" + data +
                '}';
    }
}
