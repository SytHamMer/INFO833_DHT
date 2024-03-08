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
        for (int i = 0; i<10; i++){
            dht.newNode(i, rand.nextInt(100));
        }

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


        //First events
        dht.getEvents().add(new Evenement(0, 11, new Message("join", "request", 0, null), dht));





        //Main loop
        int cpt = 0;
        while (dht.getCurrentTime()<end_time || dht.getEvents().size()>0){
            System.out.println("Event : " + cpt);
            System.out.println("Current time : " + dht.getCurrentTime());
            cpt++;
            Evenement event = dht.getEvents().poll();
            event.execute();
            dht.setCurrentTime(event.getExecuteTime());



        }
    }
}
