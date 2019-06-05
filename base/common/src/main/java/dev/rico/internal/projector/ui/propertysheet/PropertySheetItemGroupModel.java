package dev.rico.internal.projector.ui.propertysheet;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;


public class PropertySheetItemGroupModel extends IdentifiableModel {
    private Property<String> name;
    private Property<String> image;
    private Property<Boolean> expanded;

    private ObservableList<PropertySheetItemModel> items;

    public ObservableList<PropertySheetItemModel> getItems() {
        return items;
    }

    public String getImage() {
        return image.get();
    }

    public void setImage(String image) { this.image.set(image);
    }

    public Property<String> imageProperty() {
        return image;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Boolean getExpanded() {
        return expanded.get();
    }

    public void setExpanded(Boolean expanded) {
        this.expanded.set(expanded);
    }

    public Property<Boolean> expandedProperty() {
        return expanded;
    }
}
