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
        for (int i = 0; i<3; i++){
            int id = i+3;
            dht.newNode(id, rand.nextInt(100));
        }



        //Set up the neighbors of three nodes of beginning
        dht.newNode(0,0);
        dht.newNode(1,50);
        dht.newNode(2,100);

        //Node 11
        dht.getNodeById(0).setInfNeighbor(2, 100);
        dht.getNodeById(0).setSupNeighbor(1, 50);

        //Node 12
        dht.getNodeById(1).setInfNeighbor(0, 0);
        dht.getNodeById(1).setSupNeighbor(2, 100);

        //Node 13
        dht.getNodeById(2).setInfNeighbor(1, 50);
        dht.getNodeById(2).setSupNeighbor(0, 0);


        //Node to insert

        for (Node node : dht.getNodes()){
            if (node.getSupNeighbor().size() == 0 && node.getInfNeighbor().size() == 0){
                node.send(new Message("join", "request", node.getId(), null),0);
            }
        }

        /*
        dht.newNode(0, 53);
        //First events
        dht.getEvents().add(new Evenement(0, 11, new Message("join", "request", 0, null), dht));
        */




        //Main loop

        System.out.println("Evolution:");
        System.out.println("{'timestamp','locReceiver', 'message'}");
        while (dht.getCurrentTime()<end_time && dht.getEvents().size()>0){
            Evenement event = dht.getEvents().poll();
            event.execute();
            int time = dht.getCurrentTime();
            int locReceiver = dht.getNodeById(event.getIdReceiver()).getLoc();
            String message = event.getMessage().getType() + "_"+  event.getMessage().getSousType() + " send by " + dht.getNodeById(event.getMessage().getSenders().get(0)).getLoc() + " contenu : " + event.getMessage().getData();
            System.out.println("{" + time + "," + locReceiver + "," + message + "}");
            dht.setCurrentTime(event.getExecuteTime());

        }


        //Check

        System.out.println("Final state:");
        for (Node node : dht.getNodes()){
            System.out.println("Node " + node.getLoc() + " has for inf neighbor " + node.getInfNeighbor().get(1) + " and for sup neighbor " + node.getSupNeighbor().get(1));
        }





    }
}
