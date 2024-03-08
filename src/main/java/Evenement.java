public class Evenement implements Comparable<Evenement> {
    private int executeTime;
    private int idNode;
    private Message message;

    private DHT dht;

    public Evenement(int executeTime, int idNode, Message message, DHT dht) {
        this.executeTime = executeTime;
        this.idNode = idNode;
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

    public int getIdNode() {
        return idNode;
    }

    public Message getMessage() {
        return message;
    }

    public void execute(){
        Node node = dht.getNodeById(idNode);
        node.receive(message);

    }

}
