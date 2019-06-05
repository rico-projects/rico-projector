package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ToggleItemModel extends ItemModel {
    private Property<String> caption;
    private Property<ItemModel> graphic;
    private Property<Boolean> selected;
    private Property<String> action;

    public Boolean getSelected() {
        return selected.get() != null && selected.get();
    }

    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    public Property<Boolean> selectedProperty() {
        return selected;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public ItemModel getGraphic() {
        return graphic.get();
    }

    public void setGraphic(ItemModel graphic) {
        this.graphic.set(graphic);
    }

    public Property<ItemModel> graphicProperty() {
        return graphic;
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

}
