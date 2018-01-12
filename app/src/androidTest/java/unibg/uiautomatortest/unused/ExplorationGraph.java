package unibg.uiautomatortest.unused;


import java.util.ArrayList;
import java.util.List;

public class ExplorationGraph {

    private List<WindowStatus> statuses;
    private List<Edge> edges;

    public ExplorationGraph(){
        statuses=new ArrayList<>();
        edges=new ArrayList<>();
    }

    public void addStatus(WindowStatus status){
        statuses.add(status);
    }


}
