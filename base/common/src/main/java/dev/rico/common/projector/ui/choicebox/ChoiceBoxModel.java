package dev.rico.common.projector.ui.choicebox;


import dev.rico.common.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ChoiceBoxModel extends ItemModel {
    private ObservableList<ChoiceBoxItemModel> items;
    private Property<ChoiceBoxItemModel> selected;
    private Property<String> action;

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

    public ObservableList<ChoiceBoxItemModel> getItems() {
        return items;
    }

    public ChoiceBoxItemModel getSelected() {
        return selected.get();
    }

    public Property<ChoiceBoxItemModel> selectedProperty() {
        return selected;
    }

    public void setSelected(ChoiceBoxItemModel selected) {
        this.selected.set(selected);
    }
}
