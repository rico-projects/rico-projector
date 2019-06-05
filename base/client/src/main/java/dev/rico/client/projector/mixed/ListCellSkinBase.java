package dev.rico.client.projector.mixed;

import dev.rico.common.projector.ui.listview.ListViewModel;
import dev.rico.common.projector.ui.menuitem.MenuItemModel;
import dev.rico.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.client.remoting.ControllerProxy;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dev.rico.client.remoting.FXBinder.bind;

public abstract class ListCellSkinBase<T> implements ListCellSkin<T> {
    private ListViewModel listViewModel;
    private ControllerProxy<?> controllerProxy;
    private EditableListCell<T> skinnable;
    private SimpleBooleanProperty editableByClickingProperty = new SimpleBooleanProperty(this, "editableByClickingProperty", true);

    public void setOwner(ListViewModel listViewModel) {
        this.listViewModel = listViewModel;
    }

    public void setControllerProxy(ControllerProxy<?> controllerProxy) {
        this.controllerProxy = controllerProxy;
    }

    @Override
    public EditableListCell<T> getSkinnable() {
        return skinnable;
    }

    @Override
    public void setSkinnable(EditableListCell<T> skinnable) {
        this.skinnable = skinnable;
    }

    @Override
    final public BooleanProperty editableByClickingProperty() {
        return editableByClickingProperty;
    }

    protected MenuItem menuItem(String text, EventHandler<ActionEvent> value) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(value);
        return menuItem;
    }

    protected Node overlay(EditableListCell<T> cell, Control edit, Label label) {
        Objects.requireNonNull(edit);
        Objects.requireNonNull(label);
        edit.prefWidthProperty().bind(cell.widthProperty());
        label.prefWidthProperty().bind(cell.widthProperty());
        StackPane stackPane = new StackPane(edit, label);
        edit.visibleProperty().bind(cell.editingProperty());
        label.visibleProperty().bind(cell.editingProperty().not());
        label.setAlignment(Pos.CENTER_LEFT);
        return stackPane;
    }

    protected Node overlay(EditableListCell<T> cell, Label edit, Label label) {
        Objects.requireNonNull(edit);
        Objects.requireNonNull(label);
        edit.setAlignment(Pos.CENTER_LEFT);
        return overlay(cell, (Control) edit, label);
    }

    protected T getItem() {
        return getSkinnable().getItem();
    }

    ListViewModel getOwner() {
        return listViewModel;
    }

    ControllerProxy<?> getControllerProxy() {
        return controllerProxy;
    }

    @Override
    public List<MenuItem> createContextItems() {
        List<MenuItem> menu = new ArrayList<>();
        ListViewModel owner = getOwner();
        if (owner != null) {
            owner.getItemMenu().stream().map(this::createNestedMenuItem).forEach(menu::add);
        }
        return menu;
    }

    private MenuItem createNestedMenuItem(MenuItemModel model) {
        if (model.getItems().isEmpty()) {
            MenuItem menuItem = new MenuItem();
            bind(menuItem.textProperty()).to(model.captionProperty());
            menuItem.setOnAction(event -> {
                if (event.getTarget() == menuItem) {
                    event.consume();
                }
                if (model.getAction() != null)
                    controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(getSkinnable(), throwable));
            });
            return menuItem;
        } else {
            Menu menu = new Menu();
            menu.setOnAction(event -> {
                if (event.getTarget() == menu) {
                    event.consume();
                    menu.getParentPopup().hide();
                }
            });
            bind(menu.textProperty()).to(model.captionProperty());
            bind(menu.getItems()).to(model.getItems(), this::createNestedMenuItem);
            return menu;
        }
    }
}
