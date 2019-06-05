package dev.rico.client.projector.uimanager.presenter;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;

public interface ViewPresenter {

    /**
     * Liefert die anzuzeigende View zurück. Sie könnte
     * z.B. in einem Tab erscheinen.
     */
    Node getView();

    /**
     * Liefert das Property zurück, welches Auskunft
     * darüber gibt ob die View gerade beschäftigt ist.
     * Abhängig davon wird ggf. neben dem Tab ein
     * ProgressIndicator angezeigt.
     */
    ReadOnlyBooleanProperty isWorkingProperty();
}
