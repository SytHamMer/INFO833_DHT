import java.util.ArrayList;

public class Message {
    private String type;
    private String sousType;
    private ArrayList<Integer> senders;

    private String data; //maybe change the type

    public Message(String type, String sousType, Integer sender, String data) {
        this.type = type;
        this.sousType = sousType;
        this.senders = new ArrayList<>();
        this.senders.add(sender);
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getSousType() {
        return sousType;
    }

    public void addSender(Integer sender) {
        this.senders.add(sender);
    }

    public ArrayList<Integer> getSenders() {
        return senders;
    }

    public String getData() {
        return data;
    }


}
