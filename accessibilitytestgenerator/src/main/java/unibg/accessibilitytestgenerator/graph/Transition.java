package unibg.accessibilitytestgenerator.graph;

/**
 * Describes the transition to a status to another.
 * It contains the action to do and the node to act upon.
 */
public class Transition {

    private UIActions action;
    private Node node;

    public Transition(UIActions action, Node node) {
        this.action = action;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public UIActions getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "action=" + action +
                ", node=" + node +
                '}';
    }
}
