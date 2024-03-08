import java.util.ArrayList;
import java.util.HashMap;

public class Message {
    private String type;
    private String sousType;
    //id et loc
    private ArrayList<Integer> senders;

    private HashMap<String, String> data; //maybe change the type
    //first one
    public Message(String type, String sousType, Integer idSender, HashMap<String, String> data) {
        this.type = type;
        this.sousType = sousType;
        this.senders = new ArrayList<>();
        this.senders.add(idSender);
        this.data = data;
    }

    //Not first one
    public Message(String type, String sousType, ArrayList<Integer> senders, HashMap<String, String> data) {
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

    public void addSender(Integer idSender) {
        this.senders.add(idSender);
    }

    public ArrayList<Integer> getSenders() {
        return senders;
    }
    //maybe change the type (HashMap<Integer, String>)
    public HashMap<String, String> getData() {
        return data;
    }


}
