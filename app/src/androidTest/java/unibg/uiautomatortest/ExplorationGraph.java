package unibg.uiautomatortest;


import java.util.ArrayList;
import java.util.List;

public class ExplorationGraph {

    private List<WindowStatus> statuses;
    private List<Transition> transitions;

    public ExplorationGraph(){
        statuses=new ArrayList<>();
        transitions=new ArrayList<>();
    }



}
