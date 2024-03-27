import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
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

    private boolean isWaiting;

    private ArrayList<Message> waitingQueue;


    public Node(int id, int loc, DHT dht) {
        this.id = id;
        this.loc = loc;
        this.supNeighbor = new ArrayList<Integer>();
        this.infNeighbor = new ArrayList<Integer>();
        this.dht =  dht;
        this.data = new HashMap<Integer, String>();
        this.isWaiting = true;
        this.waitingQueue = new ArrayList<Message>();

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


    public boolean getIsWaiting(){
        return this.isWaiting;
    }

    public ArrayList<Message> getWaitingQueue() {
        return waitingQueue;
    }

    public void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    @Override
    public String toString() {
        return "Node id : " + this.id + ", Node loc : " + this.loc + ", Node infNeighbor : " + this.infNeighbor + ", Node supNeighbor : " + this.supNeighbor;
    }



    public void makeAChoice(Message message){
        //function that will call the adequate method depending on the type of the message
        if (message.getType().equals("join")) {
            Node newNode = dht.getNodeById(message.getSenders().get(0));
            switch (message.getSousType()) {
                case "insert":
                    this.joinInsert(message);
                    break;
                case "request":
                    this.joinRequest(newNode, message);
                    break;
                case "ack":
                    this.joinAck(newNode, message);
                    break;
                default:
                    System.out.println("join_error");
                    break;
            }


        } else if (message.getType().equals("leave")) {
            switch (message.getSousType()) {
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

        } else {
            System.out.println("error");
        }



    }
    // for now easy version always go up but could be improved by taking the shortest way
    public void receive(Message message){
        //node will execute the message in the case where it is not waiting or if it is an ack message (telling him to unlock)
        if (!isWaiting  || (message.getType().equals("join") && message.getSousType().equals("ack"))) {
                this.makeAChoice(message);
        } else {
            // message can't be executed now, so it is added to the waitingQueue
            waitingQueue.add(message);
        }
    }

    //generate a random latency and add a new event based on the message received
    public void send(Message message, int receiverId) {
        Random rand = new Random();
        //random latency 10 and 50 ms possible amelioration (true calculation, not random)
        int latence = rand.nextInt(50 - 10 + 1) + 10;
        int executeTime = latence + this.dht.getCurrentTime();
        this.dht.getEvents().add(new Evenement(executeTime, receiverId, message,this.dht));


    }

    //Insert the new node to the right place of the receiver node
    public void joinInsert(Message message){
        this.isWaiting = true;
        if (message.getData().containsKey("insertIdNode")){
            //get the node to insert
            Node newNode = dht.getNodeById(Integer.parseInt(message.getData().get("insertIdNode")));

            //Send ack to the new Neighbor
            HashMap<String,String> mData = new HashMap<String,String>();
            mData.put("join_ack",message.getData().get("insertTypeNeighbor"));
            Message ack = new Message("join", "ack", this.getId(), mData);
            this.send(ack, newNode.getId());

            //change the Neighbor of the receiver node
            if (message.getData().get("insertTypeNeighbor").equals("sup")){
                this.setInfNeighbor(newNode.getId(), newNode.getLoc());
            }else{
                this.setSupNeighbor(newNode.getId(), newNode.getLoc());
            }
        }
        else{
            System.out.println("Message error, no data on the node to insert");
        }

    }


    // Acknowledge the receiver node that it has been added as neighbor, either update its neighbors or unlock itself (depending on the message)
    public void joinAck(Node newNode,Message message){
        //change the neighbor of the receiver node (sup or inf depending on the message)
        //check the data of the message
        if (message.getData().containsKey("join_ack")){
            //update node sup neighbor
            if (message.getData().get("join_ack").equals("sup")){
                this.setSupNeighbor(newNode.getId(), newNode.getLoc());
                //if the other neighbor is updated too, we can unlock both neighbors
                if (!this.getInfNeighbor().isEmpty()){
                    unlockAck();
                }
            }
            //update node inf neighbor
            else if((message.getData().get("join_ack").equals("inf"))) {
                this.setInfNeighbor(newNode.getId(), newNode.getLoc());
                //if the other neighbor is updated too, we can unlock both neighbors
                if (!this.getSupNeighbor().isEmpty()){
                    unlockAck();
                }
            }
            else if((message.getData().get("join_ack").equals("unlock"))){
                // unlock the node
                this.isWaiting = false;
                //execute the waitingQueue if there is any
                executeWaitingQueue();

            }
            else{
                System.out.println("Message error, no data on the ack type");
            }
        }
    }

    //Check if the new node should be inserted as neighbor or if the message should be transferred
    public void joinRequest(Node newNode, Message m) {
        int newLoc = newNode.getLoc();
        int newId = newNode.getId();
        // case where new node loc is superior to the receiver node loc
        if (newLoc > loc) {
            int supNeighborLoc = supNeighbor.get(1);
            // node is inserted either if its loc is inferior to the loc of the superior neighbor
            // or if the receiver is superior to its neighbor (new node is the new max)
            if (supNeighborLoc > newLoc || loc > supNeighborLoc) {
                sendInsertAck(newId, newLoc, "sup", "inf", supNeighbor.get(0));
            } else {
                //transferred to neighbor et execution of the waiting queue
                m.addSender(id);
                executeWaitingQueue();
                send(m, supNeighbor.get(0));
            }

        // case where new node loc is inferior to the receiver node loc
        } else {
            int infNeighborLoc = infNeighbor.get(1);
            // node is inserted if its loc is superior to the loc of the inferior neighbor
            if (infNeighborLoc > loc) {
                sendInsertAck(newId, newLoc, "inf", "sup", infNeighbor.get(0));
            } else {
                //transferred to neighbor et execution of the waiting queue
                m.addSender(id);
                executeWaitingQueue();
                send(m, supNeighbor.get(0));
            }

        }
    }

    // Method to insert the new node once it was checked it is the right place
    public void sendInsertAck(int newId, int newLoc, String typeNeighbor, String typeAck, int idReceiver){
        // node blocked until neighbors are updated for all nodes involved
        this.isWaiting = true;
        // send the message to the neighbor to ask to update its neighbor
        HashMap<String, String> insertData = new HashMap<>();
        insertData.put("insertIdNode", Integer.toString(newId));
        insertData.put("insertTypeNeighbor", typeNeighbor); //typeNeighbor defines if the receiver is the new inf or sup neighbor of the new node
        send(new Message("join", "insert", id, insertData), idReceiver);
        // send the ack to the new node to acknowledge it has been added as neighbor
        HashMap<String, String> ackData = new HashMap<>();
        ackData.put("join_ack", typeAck); //typeAck defines if the sender is the new inf or sup neighbor
        send(new Message("join", "ack", id, ackData), newId);
        // add new node as neighbor
        this.setSupNeighbor(newId, newLoc);
    }

    // Method to execute first message in the waiting queue if there is any
    public void executeWaitingQueue(){
        if(!waitingQueue.isEmpty()) {
            Message mes = waitingQueue.get(0);
            waitingQueue.remove(0);
            this.makeAChoice(mes);
        }
    }

    // Method to unlock the node and its neighbors
    public void unlockAck(){
        HashMap<String, String> ackData = new HashMap<>();
        ackData.put("join_ack", "unlock");
        //send neighbors the ack to unlock
        send(new Message("join", "ack", id, ackData), this.getSupNeighbor().get(0));
        send(new Message("join", "ack", id, ackData), this.getInfNeighbor().get(0));
        this.isWaiting = false; //unlock the node
        this.executeWaitingQueue();

    }

}
