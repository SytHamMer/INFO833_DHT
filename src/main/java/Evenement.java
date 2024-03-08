public class Evenement implements Comparable<Evenement> {
    private int executeTime;
    private int idReceiver;
    private Message message;

    private DHT dht;

    public Evenement(int executeTime, int idReceiver, Message message, DHT dht) {
        this.executeTime = executeTime;
        this.idReceiver = idReceiver;
        this.message = message;
        this.dht =dht;
    }
    @Override
    public int compareTo(Evenement other) {
        // replace 'yourField' with the field you want to compare
        return Integer.compare(this.executeTime, other.executeTime);
    }
    public int getExecuteTime() {
        return executeTime;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public Message getMessage() {
        return message;
    }

    public void execute(){
        Node node = dht.getNodeById(idReceiver);
        node.receive(message);

    }

}
