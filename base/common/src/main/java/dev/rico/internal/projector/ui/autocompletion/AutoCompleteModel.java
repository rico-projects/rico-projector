package dev.rico.internal.projector.ui.autocompletion;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.WithPromptText;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
@ForRemoval
public class AutoCompleteModel extends ItemModel implements WithPromptText {
    private Property<String> searchAction;
    private Property<String> action;
    private Property<String> input;
    private Property<String> promptText;
    private Property<Integer> prefColumnCount;
    private Property<AutoCompleteItemModel> selected;
    private ObservableList<AutoCompleteItemModel> items;

    public String getSearchAction() {
        return searchAction.get();
    }

    public void setSearchAction(String searchAction) {
        this.searchAction.set(searchAction);
    }

    public Property<String> searchActionProperty() {
        return searchAction;
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

    public String getInput() {
        return input.get();
    }

    public void setInput(String input) {
        this.input.set(input);
    }

    public Property<String> inputProperty() {
        return input;
    }

    public AutoCompleteItemModel getSelected() {
        return selected.get();
    }

    public void setSelected(AutoCompleteItemModel selected) {
        this.selected.set(selected);
    }

    public Property<AutoCompleteItemModel> selectedProperty() {
        return selected;
    }

    public ObservableList<AutoCompleteItemModel> getItems() {
        return items;
    }

    public String getPromptText() {
        return promptText.get();
    }

    public void setPromptText(String promptText) {
        this.promptText.set(promptText);
    }

    public Property<String> promptTextProperty() {
        return promptText;
    }

    public Integer getPrefColumnCount() {
        return prefColumnCount.get();
    }

    public void setPrefColumnCount(Integer prefColumnCount) {
        this.prefColumnCount.set(prefColumnCount);
    }

    public Property<Integer> prefColumnCountProperty() {
        return prefColumnCount;
    }
}
