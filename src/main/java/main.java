import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class main {

    public static void main(String[] args) {
        //SET UP
        int current_time = 0;
        int end_time = 1000;
        Random rand = new Random();
        PriorityQueue<Evenement> events = new PriorityQueue<>();
        DHT dht = new DHT(current_time,events);

        //Fill the dht with nodes
        //10 nodes
        /*for (int i = 0; i<10; i++){
            dht.newNode(i, rand.nextInt(100));
        }*/



        //Set up the neighbors of three nodes of beginning
        dht.newNode(11,0);
        dht.newNode(12,50);
        dht.newNode(13,100);

        //Node 11
        dht.getNodeById(11).setInfNeighbor(13, 100);
        dht.getNodeById(11).setSupNeighbor(12, 50);

        //Node 12
        dht.getNodeById(12).setInfNeighbor(11, 0);
        dht.getNodeById(12).setSupNeighbor(13, 100);

        //Node 13
        dht.getNodeById(13).setInfNeighbor(12, 50);
        dht.getNodeById(13).setSupNeighbor(11, 0);


        //Node to insert

        dht.newNode(0, 53);
        //First events
        dht.getEvents().add(new Evenement(0, 11, new Message("join", "request", 0, null), dht));





        //Main loop
        int cpt = 0;


        System.out.println("Evolution:");
        System.out.println("{'timestamp','idReceiver','message'}");
        while (dht.getCurrentTime()<end_time && dht.getEvents().size()>0){
            cpt++;
            Evenement event = dht.getEvents().poll();
            event.execute();
            int time = dht.getCurrentTime();
            int idReceiver = event.getIdReceiver();
            String message = event.getMessage().getType() + "_"+  event.getMessage().getSousType() + " send by " + event.getMessage().getSenders().get(0) + " contenu : " + event.getMessage().getData();
            System.out.println("{" + time + "," + idReceiver + "," + message + "}");
            dht.setCurrentTime(event.getExecuteTime());

        }


        //Check

        System.out.println("Final state:");
        for (Node node : dht.getNodes()){
            System.out.println("Node " + node.getLoc() + " has for inf neighbor " + node.getInfNeighbor().get(1) + " and for sup neighbor " + node.getSupNeighbor().get(1));
        }





    }
}
