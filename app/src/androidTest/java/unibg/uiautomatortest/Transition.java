package unibg.uiautomatortest;


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
}
