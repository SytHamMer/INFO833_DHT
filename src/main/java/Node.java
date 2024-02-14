import java.util.HashMap;

public class Node {
    private int id;
    private int loc;
    private HashMap<Integer, Integer> neighbors;
    private HashMap<Integer, String> data;


    public Node(int id, int loc) {
        this.id = id;
        this.loc = loc;
        this.neighbors = new HashMap<Integer, Integer>();
        this.data = new HashMap<Integer, String>();
    }

    public void setNeighbors(HashMap<Integer, Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public void setData(HashMap<Integer, String> data) {
        this.data = data;
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
    public void deliver(Message message){
        switch (message.getType()){
            case "join":
                switch (message.getSousType()){
                    case "insert":
                        System.out.println("join_insert");
                }




        }

    }




}
