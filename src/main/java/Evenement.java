public class Evenement {
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
        node.deliver(message);

    }

}
