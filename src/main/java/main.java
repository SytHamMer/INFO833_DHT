import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class main {

    public static void main(String[] args) {
        //VARIABLES TO MODIFY FOR TESTING
        int NBNODES = 100;
        int MAXLOC = 100;
        int NODE0LOC = 25;
        int NODE1LOC = 50;
        int NODE2LOC = 100;

        //SET UP
        int current_time = 0;
        int end_time = 1000000000;
        Random rand = new Random();
        PriorityQueue<Evenement> events = new PriorityQueue<>();
        DHT dht = new DHT(current_time,events);

        // To keep track of the locations of the nodes and avoid duplicates
        ArrayList<Integer> currentLocs = new ArrayList<>();


        //Set up the first three nodes
        dht.newNode(0,NODE0LOC);
        dht.newNode(1,NODE1LOC);
        dht.newNode(2,NODE2LOC);
        currentLocs.add(NODE0LOC);
        currentLocs.add(NODE1LOC);
        currentLocs.add(NODE2LOC);
        // IsWaiting set to false because neighbors where added manually
        dht.getNodeById(0).setIsWaiting(false);
        dht.getNodeById(1).setIsWaiting(false);
        dht.getNodeById(2).setIsWaiting(false);

        //Node 0
        dht.getNodeById(0).setInfNeighbor(2, NODE2LOC);
        dht.getNodeById(0).setSupNeighbor(1, NODE1LOC);

        //Node 1
        dht.getNodeById(1).setInfNeighbor(0, NODE0LOC);
        dht.getNodeById(1).setSupNeighbor(2, NODE2LOC);

        //Node 2
        dht.getNodeById(2).setInfNeighbor(1, NODE1LOC);
        dht.getNodeById(2).setSupNeighbor(0, NODE0LOC);



        //Fill the dht with nodes

        for (int i = 0; i<(NBNODES-3); i++){
            int id = i+3;
            int randInt = rand.nextInt(MAXLOC);
            if (currentLocs.contains(randInt)) {
                // if the location is already taken, we generate a new one
                while (currentLocs.contains(randInt)) {
                    randInt = rand.nextInt(MAXLOC);
                }
            } dht.newNode(id, randInt);
            currentLocs.add(randInt);
        }

        // Generate join request from all nodes without neighbors, to node 0 (id)
        // Could be ameliorated by sending to a random node
        for (Node node : dht.getNodes()){
            if (node.getSupNeighbor().isEmpty() && node.getInfNeighbor().isEmpty()){
                node.send(new Message("join", "request", node.getId(), null),0);
            }
        }

        //Main loop
        System.out.println("Evolution:");
        System.out.println("{'timestamp','locReceiver', 'message'}");
        while (dht.getCurrentTime()<end_time && !dht.getEvents().isEmpty()){
            Evenement event = dht.getEvents().poll();
            event.execute();
            int time = dht.getCurrentTime();
            int locReceiver = dht.getNodeById(event.getIdReceiver()).getLoc();
            String message = event.getMessage().getType() + "_"+  event.getMessage().getSousType() + " send by " + dht.getNodeById(event.getMessage().getSenders().get(0)).getLoc() + " contenu : " + event.getMessage().getData();
            System.out.println("{" + time + "," + locReceiver + "," + message + "}");
            dht.setCurrentTime(event.getExecuteTime());

        }


        //Check if all nodes have neighbors, else it means the algorithm failed
        boolean check = true;

        System.out.println("Final state:");
        for (Node node : dht.getNodes()){
            System.out.println(node);

            if (node.getSupNeighbor().isEmpty()) {
                System.out.println("Node " + node.getLoc() + " has no sup neighbor");
                check = false;
            }else if (node.getInfNeighbor().isEmpty()){
                System.out.println("Node " + node.getLoc() + " has no inf neighbor");
                check = false;
            }
            else {
                System.out.println("Node " + node.getLoc() + " has for inf neighbor " + node.getInfNeighbor().get(1) + " and for sup neighbor " + node.getSupNeighbor().get(1));
            }
        }

        System.out.println("Check: " + check);






    }
}
