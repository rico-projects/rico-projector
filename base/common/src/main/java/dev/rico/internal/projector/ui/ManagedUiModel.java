package dev.rico.internal.projector.ui;


import dev.rico.internal.projector.mixed.RemotingEvent;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ManagedUiModel {

    private Property<ItemModel> root;
    private Property<DialogModel> dialog;
    private Property<ItemModel> focusedItem;
    private Property<Boolean> isWorking;
    private Property<RemotingEvent> event;
    private ObservableList<IdentifiableModel> retainedModels;
    private Property<Integer> badgeCount;

    public ItemModel getRoot() {
        return root.get();
    }

    public void setRoot(ItemModel itemModel) {
        root.set(itemModel);
    }

    public Property<ItemModel> rootProperty() {
        return root;
    }

    public ItemModel getFocusedItem() {
        return focusedItem.get();
    }

    public void setFocusedItem(ItemModel focusedItem) {
        this.focusedItem.set(focusedItem);
    }

    public Property<ItemModel> focusedItemProperty() {
        return focusedItem;
    }

    public RemotingEvent getEvent() {
        return event.get();
    }

    public void setEvent(RemotingEvent event) {
        this.event.set(event);
    }

    public Property<RemotingEvent> eventProperty() {
        return event;
    }

    public Boolean getIsWorking() {
        return isWorking.get() != null && isWorking.get();
    }

    public void setIsWorking(Boolean isWorking) {
        this.isWorking.set(isWorking);
    }

    public Property<Boolean> isWorkingProperty() {
        return isWorking;
    }

    public void showDialog(DialogModel dialog) {
        this.dialog.set(dialog);
    }

    public Property<DialogModel> dialogProperty() {
        return dialog;
    }

    public ObservableList<IdentifiableModel> getRetainedModels() {
        return retainedModels;
    }

    public Integer getBadgeCount() {
        return badgeCount.get();
    }

    public void setBadgeCount(Integer badgeCount) {
        this.badgeCount.set(badgeCount);
    }

    public Property<Integer> badgeCountProperty() {
        return badgeCount;
    }

}
