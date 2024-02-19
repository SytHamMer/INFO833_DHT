import java.util.Comparator;
//Change the comparator to compare the execute time of the events
// cf send in Node
public class EventComparator implements Comparator<Evenement>{
    @Override
    public int compare(Evenement e1, Evenement e2){
        return Integer.compare(e1.getExecuteTime(), e2.getExecuteTime());

    }
}
