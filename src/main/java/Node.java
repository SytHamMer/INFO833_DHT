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


    // for now easy version always go up but could be improved by taking the shortest way
    public void deliver(Message message){
        switch (message.getType()){
            case "join":
                Node newNode = dht.getNodeById(message.getSenders().get(0));
                switch (message.getSousType()){
                    case "insert":
                        System.out.println("join_insert");


                        break;
                    case "request":
                        System.out.println("join_request");
                        joinRequest(newNode, message);
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
    public void send(Message message, int receiverId) {
        Random rand = new Random();
        //Latence aléatoire entre 10 et 50 ms amélioration possible (vrai calcul et non random)
        int latence = rand.nextInt(50 - 10 + 1) + 10;
        int executeTime = latence + dht.getCurrentTime();
        dht.getEvents().add(new Evenement(executeTime, receiverId, message,dht));


    }

    public void joinRequest(Node newNode, Message m){
        int newLoc = newNode.getLoc();
        int newId = newNode.getId();
        if(newLoc>loc){
            int supNeighborLoc = supNeighbor.get(1);
            if (supNeighborLoc>newLoc || loc> supNeighborLoc){
                HashMap<String, String> mData = new HashMap<>();
                mData.put("insertIdNode", Integer.toString(newId));
                send(new Message("join", "insert", id, mData), supNeighbor.get(0));

            } else {
                m.addSender(id);
                send(m, supNeighbor.get(0));
            }
        } else {
            int infNeighborLoc = infNeighbor.get(1);
            if (infNeighborLoc<newLoc || loc<infNeighborLoc){
                HashMap<String, String> mData = new HashMap<>();
                mData.put("insertIdNode", Integer.toString(newId));
                send(new Message("join", "insert", id, mData), infNeighbor.get(0));

            } else {
                m.addSender(id);
                send(m, supNeighbor.get(0));
            }
        }

    }


}
