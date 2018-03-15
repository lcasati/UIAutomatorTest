package unibg.accessibilitytestgenerator.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the visited statuses and the path to get to a certain status starting from the initial one
 */
public class ListGraph {


    private static HashMap<Integer, List<Transition>> paths = new HashMap<>();
    private static List<WindowStatus> visitedStatuses = new ArrayList<>();
    private static int statusNumber= 0;
    public static WindowStatus status0;

    /**
     * Add status to the visited statuses list
     * @param status new status to add
     */
    public static void addVisitedStatus(WindowStatus status){
        status.setNumber(statusNumber);
        visitedStatuses.add(status);
        statusNumber++;
    }

    /**
     * Returns the list of statuses already visited
     * @return List of WindowStatus
     */
    public static List<WindowStatus> getVisitedStatuses() {
        return visitedStatuses;
    }

    /**
     * Add path to get to a certain status.
     * The path is made of the list of Transition to do in order to get from the initial status to the one indicated by statusNumber
     * @param statusNumber the number of the destination status
     * @param transitionList the List of Transition with the actions to do
     */
    public static void addPath(int statusNumber, List<Transition> transitionList){
        paths.put(statusNumber,transitionList);
    }

    /**
     * Returns List of actions to do to get to a certain status
     * @param statusNumber the number of the destination status
     * @return List of Transition to get to the destination status
     */
    public static List<Transition> getPath(int statusNumber){
        return paths.get(statusNumber);
    }

}
