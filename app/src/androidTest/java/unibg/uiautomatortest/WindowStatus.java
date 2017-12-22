package unibg.uiautomatortest;


import android.support.test.uiautomator.UiObject2;

import java.util.ArrayList;
import java.util.List;

public class WindowStatus {

    private List<Node> nodes;
    private List<Transition> transitions;

    public WindowStatus(List<UiObject2> list){
        nodes=new ArrayList<>();
        for(UiObject2 obj:list){
            nodes.add(new Node(obj));
        }
        transitions= new ArrayList<>();
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WindowStatus that = (WindowStatus) o;

        return nodes != null ? nodes.equals(that.nodes) : that.nodes == null;
    }

}
