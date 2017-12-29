package unibg.uiautomatortest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListGraph {


    private static HashMap<Integer, List<Transition>> paths = new HashMap<>();
    private static List<WindowStatus> visitedStatuses = new ArrayList<>();
    private static int statusNumber= 0;
    public static WindowStatus status0;


    public static void addVisitedStatus(WindowStatus status){
        status.setNumber(statusNumber);
        visitedStatuses.add(status);
        statusNumber++;
    }

    public static List<WindowStatus> getVisitedStatuses() {
        return visitedStatuses;
    }

    public static void addPath(int statusNumber, List<Transition> transitionList){
        paths.put(statusNumber,transitionList);
    }

    public static List<Transition> getPath(int statusNumber){
        return paths.get(statusNumber);
    }
}
