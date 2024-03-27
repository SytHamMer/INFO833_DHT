import java.util.ArrayList;
import java.util.PriorityQueue;


public class DHT {

    private ArrayList<Node> nodes;

    private int currentTime;

    private PriorityQueue<Evenement> events;

    public DHT(int currentTime,PriorityQueue<Evenement> events) {
        this.nodes = new ArrayList<Node>();
        this.currentTime = currentTime;
        this.events = events;
    }

    // Add new node to node list
    public void newNode(int id, int loc){
        Node node = new Node(id, loc,this);
        this.nodes.add(node);
    }

    public int getCurrentTime() {
        return currentTime;
    }

    // Return node corresponding to id given
    public Node getNodeById(int id){
        for (Node node : this.nodes){
            if (node.getId() == id){
                return node;
            }
        }
        return null;
    }

    public ArrayList<Node> getNodes(){
        return this.nodes;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    // Add new event to event list
    public void addEvent(Evenement event){
        this.events.add(event);
    }

    public PriorityQueue<Evenement> getEvents(){
        return this.events;
    }

}
