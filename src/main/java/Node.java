import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;


public class Node {
    private int id;
    private int loc;
    //id,loc
    private ArrayList<Integer> supNeighbor;
    //id,loc
    private ArrayList<Integer> infNeighbor;

    private HashMap<Integer, String> data;


    public Node(int id, int loc) {
        this.id = id;
        this.loc = loc;
        this.supNeighbor = new ArrayList<Integer>();
        this.infNeighbor = new ArrayList<Integer>();

        this.data = new HashMap<Integer, String>();
    }

    public void setInfNeighbor(int id, int loc){
        this.infNeighbor.add(id);
        this.infNeighbor.add(loc);
    }

    public void setSupNeighbor(int id, int loc){
        this.supNeighbor.add(id);
        this.supNeighbor.add(loc);
    }


    public void setData(HashMap<Integer, String> data) {
        this.data = data;
    }

    public ArrayList<Integer> getInfNeighbor() {
        return infNeighbor;
    }

    public ArrayList<Integer> getSupNeighbor() {
        return supNeighbor;
    }

    public int getId() {
        return id;
    }

    public int getLoc() {
        return loc;
    }

    public Node getById(int id){
        return this;
    }

    public Node getByLoc(int loc){
        return this;
    }


    // for now easy version always go up but could be improved by taking the shortest way
    public void deliver(Message message){
        switch (message.getType()){
            case "join":
                switch (message.getSousType()){
                    case "insert":
                        System.out.println("join_insert");
                        Node node = this.getById( this.getSupNeighbor().get(0));
                        //Send ack to the old leftNeighbor
                        //Send method needs DHT and events list ? Normal ? Not opti ?
                        Message ack = new Message("join", "ack", this.getInfNeighbor().get(0), this.getInfNeighbor().get(1), null);


                        //change the infNeighbor of the receiver node
                        node.setInfNeighbor(message.getSenders().get(0), message.getSenders().get(1));


                        break;
                    case "request":
                        System.out.println("join_request");

                        break;
                    case "ack":
                        System.out.println("join_ack");
                        break;
                    default:
                        System.out.println("join_error");
                        break;

                        }
                //POURQUOI L'INDENTATION EST CASSEE ???
                    case "leave":
                        switch (message.getSousType()){
                            case "insert":
                                System.out.println("leave_insert");
                                break;
                            case "request":
                                System.out.println("leave_request");
                                break;
                            default:
                                System.out.println("leave_error");
                        }
                        break;


                default:
                    System.out.println("error");



        }

    }

    //génére l'event + calcul latence
    public void send(Message message, int id, PriorityQueue<Evenement> evnts, DHT dht) {
        Random rand = new Random();
        //Latence aléatoire entre 10 et 50 ms amélioration possible (vrai calcul et non random)
        int latence = rand.nextInt(50 - 10 + 1) + 10;
        int executeTime = latence + dht.getCurrentTime();
        evnts.add(new Evenement(executeTime, id, message,dht));


    }




}
