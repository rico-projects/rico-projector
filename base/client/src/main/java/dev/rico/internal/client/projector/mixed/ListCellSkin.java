package dev.rico.internal.client.projector.mixed;

import dev.rico.internal.projector.ui.listview.ListViewModel;
import dev.rico.client.remoting.ControllerProxy;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.List;

public interface ListCellSkin<T> {
    void setOwner(ListViewModel listViewModel);
    void setControllerProxy(ControllerProxy<?> controllerProxy);

    EditableListCell<T> getSkinnable();

    void setSkinnable(EditableListCell<T> skinnable);

    void setContent(T item);

    void clearContent();

    void commit(T newValue);

    void takeFocus();

    void reset(T item);

    List<MenuItem> createContextItems();

    Node createContent();

    BooleanProperty editableByClickingProperty();
}
