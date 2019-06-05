package dev.rico.internal.projector.ui.propertysheet;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;


public class PropertySheetModel extends ItemModel {
    private ObservableList<PropertySheetItemGroupModel> groups;
    private Property<Boolean> hideNullItems;
    private Property<Boolean> editing;
    private Property<Skin> skin;

    public ObservableList<PropertySheetItemGroupModel> getGroups() {
        return groups;
    }

    public Boolean getEditing() {
        return editing.get();
    }

    public void setEditing(Boolean editing) {
        this.editing.set(editing);
    }

    public Property<Boolean> editingProperty() {
        return editing;
    }

    public Boolean getHideNullItems() {
        return hideNullItems.get();
    }

    public void setHideNullItems(Boolean hideNullItems) {
        this.hideNullItems.set(hideNullItems);
    }

    public Property<Boolean> hideNullItemsProperty() {
        return hideNullItems;
    }

    public Skin getSkin() {
        return skin.get();
    }

    public void setSkin(Skin skin) {
        this.skin.set(skin);
    }

    public Property<Skin> skinProperty() {
        return skin;
    }

    public enum Skin {
        ExpandableGroups, NoGroups, MasterDetail
    }
}
