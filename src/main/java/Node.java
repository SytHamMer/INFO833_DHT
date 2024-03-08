import java.util.ArrayList;
import java.util.HashMap;
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
        this.infNeighbor.clear();
        this.infNeighbor.add(id);
        this.infNeighbor.add(loc);
    }

    public void setSupNeighbor(int id, int loc){
        this.supNeighbor.clear();
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
    public void receive(Message message){



        if (message.getType() == "join"){
            Node newNode = dht.getNodeById(message.getSenders().get(0));
            switch (message.getSousType()){
                case "insert":
                    this.joinInsert(message);
                    break;
                case "request":
                    this.joinRequest(newNode,message);
                    break;
                case "ack":
                    this.joinAck(newNode,message);
                    break;
                default:
                    System.out.println("join_error");
                    break;
            }


        }
        else if (message.getType() == "leave"){
            switch (message.getSousType()){
                case "exit":
                    //change my neighbor with the data of the message
                    //Send ack to new neighbor
                    break;
                case "ack":
                    //change my neighbor with the node of the message
                    break;
                default:
                    System.out.println("leave_error");
                    break;
            }

        }

        else{
            System.out.println("error");
        }
    }

    //génére l'event + calcul latence
    public void send(Message message, int receiverId) {
        Random rand = new Random();
        //Latence aléatoire entre 10 et 50 ms amélioration possible (vrai calcul et non random)
        int latence = rand.nextInt(50 - 10 + 1) + 10;
        int executeTime = latence + this.dht.getCurrentTime();
        this.dht.getEvents().add(new Evenement(executeTime, receiverId, message,this.dht));


    }

    //Insert the new node to the right place of the receiver node
    public void joinInsert(Message message){

        if (message.getData().containsKey("insertIdNode")){
            //get the node to insert

            Node newNode = dht.getNodeById(Integer.parseInt(message.getData().get("insertIdNode")));
            //Send ack to the new infNeighbor
            HashMap<String,String> mData = new HashMap<String,String>();
            mData.put("join_ack","sup");
            Message ack = new Message("join", "ack", this.getId(), mData);
            this.send(ack, newNode.getId());
            //change the infNeighbor of the receiver node
            this.setInfNeighbor(newNode.getId(), newNode.getLoc());
        }
        else{
            System.out.println("Erreur dans le message pas d'information sur le node a inserer");
        }

    }


    //Update our neighbor as he updated his neighbor
    public void joinAck(Node newNode,Message message){
        //change the neighbor of the receiver node (sup or inf depending on the message)

        //check the data of the message
        if (message.getData().containsKey("join_ack")){
            //if it a sup request i update my sup neighbor
            if (message.getData().get("join_ack").equals("sup")){
                this.setSupNeighbor(newNode.getId(), newNode.getLoc());
            }
            //else I update my inf neighbor
            else if((message.getData().get("join_ack").equals("inf"))) {
                this.setInfNeighbor(newNode.getId(), newNode.getLoc());
            }
        }
    }


    public void joinRequest(Node newNode, Message m) {
        int newLoc = newNode.getLoc();
        int newId = newNode.getId();
        if (newLoc > loc) {
            int supNeighborLoc = supNeighbor.get(1);
            if (supNeighborLoc > newLoc || loc > supNeighborLoc) {
                HashMap<String, String> insertData = new HashMap<>();
                insertData.put("insertIdNode", Integer.toString(newId));
                send(new Message("join", "insert", id, insertData), supNeighbor.get(0));
                HashMap<String, String> ackData = new HashMap<>();
                ackData.put("join_ack", "inf");
                send(new Message("join", "ack", id, ackData), newId);
                this.setSupNeighbor(newId, newLoc);

            } else {
                m.addSender(id);
                send(m, supNeighbor.get(0));
            }
        } else {
            int infNeighborLoc = infNeighbor.get(1);
            if (infNeighborLoc < newLoc || loc < infNeighborLoc) {
                HashMap<String, String> insertData = new HashMap<>();
                insertData.put("insertIdNode", Integer.toString(newId));
                send(new Message("join", "insert", id, insertData), infNeighbor.get(0));
                HashMap<String, String> ackData = new HashMap<>();
                ackData.put("join_ack", "sup");
                send(new Message("join", "ack", id, ackData), newId);
                this.setInfNeighbor(newId, newLoc);

            } else {
                m.addSender(id);
                send(m, supNeighbor.get(0));
            }

        }
    }





    @Override
    public String toString() {
        return "Node id : " + this.id + ", Node loc : " + this.loc;
    }





}
