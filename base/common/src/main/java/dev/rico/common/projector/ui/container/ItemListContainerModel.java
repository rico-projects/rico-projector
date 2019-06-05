package dev.rico.common.projector.ui.container;

import dev.rico.common.projector.ui.ItemModel;
import dev.rico.common.projector.ui.WithPadding;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


/**
 * Ein Container der Item-Listen verwaltet.
 */
@RemotingBean
public abstract class ItemListContainerModel<ITEM_TYPE> extends ItemModel implements WithPadding {
    private Property<Integer> padding;

    public Integer getPadding() {
        return padding.get();
    }

    public void setPadding(Integer padding) {
        this.padding.set(padding);
    }

    public Property<Integer> paddingProperty() {
        return padding;
    }

    public void add(ITEM_TYPE itemModel) {
        getItems().add(itemModel);
    }

    public abstract ObservableList<ITEM_TYPE> getItems();

    @SafeVarargs
    public final void addAll(ITEM_TYPE... itemModel) {
        getItems().addAll(itemModel);
    }

    public void clear() {
        getItems().clear();
    }
}
