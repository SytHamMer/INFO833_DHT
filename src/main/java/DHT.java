import java.util.ArrayList;

public class DHT {

    private ArrayList<Node> nodes;

    private int currentTime;

    public DHT(int currentTime) {
        this.nodes = new ArrayList<Node>();
        this.currentTime = currentTime;
    }

    public void newNode(int id, int loc){
        Node node = new Node(id, loc);
        this.nodes.add(node);
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public Node getNodeById(int id){
        for (Node node : this.nodes){
            if (node.getId() == id){
                return node;
            }
        }
        return null;
    }

}
