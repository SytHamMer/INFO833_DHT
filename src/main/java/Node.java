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

    public DHT dht;

    public Node(int id, int loc, DHT dht) {
        this.id = id;
        this.loc = loc;
        this.supNeighbor = new ArrayList<Integer>();
        this.infNeighbor = new ArrayList<Integer>();
        this.dht =  dht;
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
                        Message ack = new Message("join", "ack", this.getInfNeighbor().get(0), null);
                        //Need to know how to access to events
                        this.send(ack, this.getSupNeighbor().get(0), dht.getEvents(), dht);
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
            case "leave":
                switch (message.getSousType()){
                    case "exit":
                        System.out.println("leave_exit");
                        //change my neighbor with the data of the message



                        //Send ack to new neighbor


                        break;


                    case "ack":
                            System.out.println("leave_ack");

                            //change my neighbor with the node of the message
                            break;

                    default:
                        System.out.println("leave_error");
                        break;
                        }

                        break;


                default:
                    System.out.println("error");



        }

    }

    //génére l'event + calcul latence
    public void send(Message message, int id, PriorityQueue<Evenement> events, DHT dht) {
        Random rand = new Random();
        //Latence aléatoire entre 10 et 50 ms amélioration possible (vrai calcul et non random)
        int latence = rand.nextInt(50 - 10 + 1) + 10;
        int executeTime = latence + dht.getCurrentTime();
        events.add(new Evenement(executeTime, id, message,dht));


    }




}
