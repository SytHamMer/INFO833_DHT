import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class main {

    public static void main(String[] args) {
        int current_time = 0;
        int end_time = 1000;
        Random rand = new Random();
        PriorityQueue<Evenement> evnts = new PriorityQueue<>();
        DHT dht = new DHT(current_time);




        while (current_time<end_time || evnts.size()>0){
            System.out.println("ououou");
            Evenement evnt = evnts.poll();
            current_time += evnt.getExecuteTime();



        }
    }
}
