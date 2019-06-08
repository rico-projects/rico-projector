package to.remove.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class HBoxFillComponent extends Pane {
    public HBoxFillComponent() {
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
