package unibg.uiautomatortest.unused;


public class Edge {

    private WindowStatus startStatus;
    private WindowStatus destStatus;
    private Transition transition;

    public Edge(WindowStatus startStatus, WindowStatus destStatus, Transition transition) {
        this.startStatus = startStatus;
        this.destStatus = destStatus;
        this.transition = transition;
    }


    public WindowStatus getStartStatus() {
        return startStatus;
    }

    public WindowStatus getDestStatus() {
        return destStatus;
    }

    public Transition getTransition() {
        return transition;
    }
}
