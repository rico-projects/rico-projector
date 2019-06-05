package to.remove.uimanager;

import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MenuButton extends Button {

    private final ContextMenu menu = new ContextMenu();

    public MenuButton() {
        setOnAction(event -> menu.show(this, Side.LEFT, 25, 0));
    }

    final public ObservableList<MenuItem> getItems() {
        return menu.getItems();
    }
}
