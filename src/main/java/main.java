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




        //First events
        dht.getEvents().add(new Evenement(0, 0, new Message("join", "request", 0, 0), dht));





        //Main loop
        int cpt = 0;
        while (dht.getCurrentTime()<end_time || dht.getEvents().size()>0){
            System.out.println("Event + " + cpt);
            cpt++;
            Evenement event = dht.getEvents().poll();
            event.execute();
            dht.setCurrentTime(event.getExecuteTime());



        }
    }
}
