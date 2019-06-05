package dev.rico.common.projector.ui.listview;

import dev.rico.common.projector.ui.container.ItemListContainerModel;
import dev.rico.common.projector.ui.menuitem.MenuItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;

public class ListViewModel extends ItemListContainerModel<ListViewItemModel> {
    private ObservableList<ListViewItemModel> items;
    private Property<ListViewItemModel> selected;
    private Property<String> selectedAction;
    private ObservableList<MenuItemModel> itemMenu;
    private Property<String> rendererClass;

    @Override
    public ObservableList<ListViewItemModel> getItems() {
        return items;
    }

    public String getRendererClass() {
        return rendererClass.get();
    }

    public void setRendererClass(String rendererClass) {
        this.rendererClass.set(rendererClass);
    }

    public Property<String> rendererClassProperty() {
        return rendererClass;
    }

    public ListViewItemModel getSelected() {
        return selected.get();
    }

    public void setSelected(ListViewItemModel selected) {
        this.selected.set(selected);
    }

    public Property<ListViewItemModel> selectedProperty() {
        return selected;
    }

    public ObservableList<MenuItemModel> getItemMenu() {
        return itemMenu;
    }

    public String getSelectedAction() {
        return selectedAction.get();
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction.set(selectedAction);
    }

    public Property<String> selectedActionProperty() {
        return selectedAction;
    }
}
