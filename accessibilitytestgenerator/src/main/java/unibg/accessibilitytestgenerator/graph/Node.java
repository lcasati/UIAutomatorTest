package unibg.accessibilitytestgenerator.graph;


import android.graphics.Rect;
import android.support.test.uiautomator.UiObject2;

/**
 * Represents the node used by Android to identify graphic components in a window.
 * It mirrors UIObject2 from UIAutomator
 */
public class Node {

    private String text;
    private String className;
    private String packageName;
    private String contentDesc;
    private String resourceName;
    private String applicationPackage;
    private Rect bounds;
    private boolean checkable;
    private boolean checked;
    private boolean clickable;
    private boolean enabled;
    private boolean focusable;
    private boolean focused;
    private boolean scrollable;
    private boolean long_clickable;
    private boolean selected;


    public Node(UiObject2 node) {
        this.text = node.getText();
        this.className = node.getClassName();
        this.packageName = node.getApplicationPackage();
        this.contentDesc = node.getContentDescription();
        this.resourceName = node.getResourceName();
        this.applicationPackage = node.getApplicationPackage();
        this.bounds = node.getVisibleBounds();
        this.checkable = node.isCheckable();
        this.checked = node.isChecked();
        this.clickable = node.isClickable();
        this.enabled = node.isEnabled();
        this.focusable = node.isFocusable();
        this.focused = node.isFocused();
        this.scrollable = node.isScrollable();
        this.long_clickable = node.isLongClickable();
        this.selected = node.isSelected();
    }


    public String getText() {
        return text;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getApplicationPackage() {
        return applicationPackage;
    }

    public Rect getBounds() {
        return bounds;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isClickable() {
        return clickable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public boolean isLong_clickable() {
        return long_clickable;
    }

    public boolean isSelected() {
        return selected;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (checkable != node.checkable) return false;
        if (checked != node.checked) return false;
        if (clickable != node.clickable) return false;
        if (enabled != node.enabled) return false;
        if (focusable != node.focusable) return false;
        if (focused != node.focused) return false;
        if (scrollable != node.scrollable) return false;
        if (long_clickable != node.long_clickable) return false;
        if (selected != node.selected) return false;
        if (text != null ? !text.equals(node.text) : node.text != null) return false;
        if (className != null ? !className.equals(node.className) : node.className != null)
            return false;
        if (packageName != null ? !packageName.equals(node.packageName) : node.packageName != null)
            return false;
        if (contentDesc != null ? !contentDesc.equals(node.contentDesc) : node.contentDesc != null)
            return false;
        return bounds != null ? bounds.equals(node.bounds) : node.bounds == null;
    }


    @Override
    public String toString() {
        return "Node{" +
                "text='" + text + '\'' +
                ", className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", contentDesc='" + contentDesc + '\'' +
                ", bounds=" + bounds +
                ", checkable=" + checkable +
                ", checked=" + checked +
                ", clickable=" + clickable +
                ", enabled=" + enabled +
                ", focusable=" + focusable +
                ", focused=" + focused +
                ", scrollable=" + scrollable +
                ", long_clickable=" + long_clickable +
                ", selected=" + selected +
                '}';
    }
}
