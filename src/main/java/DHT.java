import java.util.ArrayList;

public class DHT {

    private ArrayList<Node> nodes;

    public DHT() {
        this.nodes = new ArrayList<Node>();
    }

    public void newNode(int id, int loc){
        Node node = new Node(id, loc);
        this.nodes.add(node);
    }

}
