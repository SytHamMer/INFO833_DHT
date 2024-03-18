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


    public void makeAChoice(Message message){
        //function that will make a choice depending on the message
        if (message.getType() == "join") {
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


        } else if (message.getType() == "leave") {
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
        System.out.println("WaitingQueue actuelle du node " + this.getLoc() + " : " + this.waitingQueue);
        //change this if with a boolean variable ?
        if (!isWaiting  || (message.getType().equals("join") && message.getSousType().equals("ack"))) {
                this.makeAChoice(message);
        } else {
            waitingQueue.add(message);
            System.out.println(loc + " " + waitingQueue);

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
        this.isWaiting = true;
        System.out.println(loc + " " + isWaiting);
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
            System.out.println("Erreur dans le message pas d'information sur le node a inserer");
        }

        System.out.println(loc + " " + isWaiting);
    }


    //Update our neighbor as he updated his neighbor
    public void joinAck(Node newNode,Message message){
        //change the neighbor of the receiver node (sup or inf depending on the message)
        //check the data of the message
        if (message.getData().containsKey("join_ack")){
            //if it a sup request i update my sup neighbor
            if (message.getData().get("join_ack").equals("sup")){
                this.setSupNeighbor(newNode.getId(), newNode.getLoc());
                if (this.getInfNeighbor().size() > 0){
                    unlockAck();
                }
            }
            //else I update my inf neighbor
            else if((message.getData().get("join_ack").equals("inf"))) {
                this.setInfNeighbor(newNode.getId(), newNode.getLoc());
                if (this.getSupNeighbor().size() > 0){
                    unlockAck();
                }
            }
            else if((message.getData().get("join_ack").equals("unlock"))){
                this.isWaiting = false;
                System.out.println("UNLOCK");
                //execute the waitingQueue if their is any
                executeWaitingQueue();

            }
            else{
                System.out.println("Erreur dans le message pas d'information sur le type de voisin a mettre a jour");
            }
        }
    }


    public void joinRequest(Node newNode, Message m) {
        System.out.println("message : " + m + " pour : " + loc);
        int newLoc = newNode.getLoc();
        int newId = newNode.getId();
        if (newLoc > loc) {
            int supNeighborLoc = supNeighbor.get(1);
            if (supNeighborLoc > newLoc || loc > supNeighborLoc) {
                this.isWaiting = true;
                System.out.println(loc + " " + isWaiting);
                HashMap<String, String> insertData = new HashMap<>();
                insertData.put("insertIdNode", Integer.toString(newId));
                insertData.put("insertTypeNeighbor", "sup");
                send(new Message("join", "insert", id, insertData), supNeighbor.get(0));
                HashMap<String, String> ackData = new HashMap<>();
                ackData.put("join_ack", "inf");
                send(new Message("join", "ack", id, ackData), newId);
                this.setSupNeighbor(newId, newLoc);
                System.out.println(loc + " " + isWaiting);

            } else {
                m.addSender(id);
                executeWaitingQueue();
                send(m, supNeighbor.get(0));
            }
        } else {
            int infNeighborLoc = infNeighbor.get(1);
            if (infNeighborLoc > loc) {
                //EDIT : if newNode > infNeighborLoc !!!!!!!
                this.isWaiting = true;
                System.out.println(loc + " " + isWaiting);
                HashMap<String, String> insertData = new HashMap<>();
                insertData.put("insertIdNode", Integer.toString(newId));
                insertData.put("insertTypeNeighbor", "inf");
                send(new Message("join", "insert", id, insertData), infNeighbor.get(0));
                HashMap<String, String> ackData = new HashMap<>();
                ackData.put("join_ack", "sup");
                send(new Message("join", "ack", id, ackData), newId);
                this.setInfNeighbor(newId, newLoc);
                System.out.println(loc + " " + isWaiting);

            } else {
                m.addSender(id);
                executeWaitingQueue();
                send(m, supNeighbor.get(0));
            }

        }
    }



    public void executeWaitingQueue(){
        if(waitingQueue.size() > 0) {
            System.out.println("executing waiting messages");
            //Executer tous les messages ici ?
            if(waitingQueue.get(0).getSenders().size()<5) {
                System.out.println(dht.getNodeById(waitingQueue.get(0).getSenders().get(0)) + " " + waitingQueue.get(0).getSousType() + " " + waitingQueue.get(0).getData());
            }
            Message mes = waitingQueue.get(0);
            waitingQueue.remove(0);
            this.makeAChoice(mes);
            System.out.println(waitingQueue);
            System.out.println("WaintingQueue restante :" + waitingQueue);
        }
        else{
            System.out.println("No waiting messages");
        }
    }
    public void unlockAck(){
        if (this.getSupNeighbor().size()> 0 && this.getInfNeighbor().size() > 0){
            System.out.println("UNLOCK SENT");
            HashMap<String, String> ackData = new HashMap<>();
            ackData.put("join_ack", "unlock");
            send(new Message("join", "ack", id, ackData), this.getSupNeighbor().get(0));
            send(new Message("join", "ack", id, ackData), this.getInfNeighbor().get(0));
            this.isWaiting = false;
            this.executeWaitingQueue();

        }

    }

    public void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    @Override
    public String toString() {
        return "Node id : " + this.id + ", Node loc : " + this.loc + ", Node infNeighbor : " + this.infNeighbor + ", Node supNeighbor : " + this.supNeighbor;
    }

}
