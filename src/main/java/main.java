import java.util.ArrayList;
import java.util.Random;

public class main {

    public static void main(String[] args) {
        int current_time = 0;
        int end_time = 1000;
        Random rand = new Random();
        ArrayList<Evenement> evnts = new ArrayList<Evenement>();
        DHT dht = new DHT(current_time);




        while (current_time<end_time || evnts.size()>0){
            System.out.println("ououou");
            Evenement evnt = evnts.get(0);
            evnts.remove(0);
            current_time += evnt.getExecuteTime();



        }
    }
}
