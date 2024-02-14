public class Evenement {
    private int execute_time;
    private int idNode;
    private Message message;

    public Evenement(int execute_time, int idNode, Message message) {
        this.execute_time = execute_time;
        this.idNode = idNode;
        this.message = message;
    }

    public int getExecute_time() {
        return execute_time;
    }

    public int getIdNode() {
        return idNode;
    }

    public Message getMessage() {
        return message;
    }

}
