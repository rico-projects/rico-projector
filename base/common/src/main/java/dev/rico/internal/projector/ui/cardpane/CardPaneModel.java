package dev.rico.internal.projector.ui.cardpane;


import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Pos;

@RemotingBean
public class CardPaneModel extends ItemListContainerModel<CardPaneItemModel>  {
    private ObservableList<CardPaneItemModel> items;
    private Property<CardPaneItemModel> visibleItem;
    private Property<Pos> alignment;

    public Pos getAlignment() {
        return alignment.get();
    }

    public void setAlignment(Pos alignment) {
        this.alignment.set(alignment);
    }

    public Property<Pos> alignmentProperty() {
        return alignment;
    }

    @Override
    public ObservableList<CardPaneItemModel> getItems() {
        return items;
    }

    public CardPaneItemModel getVisibleItem() {
        return visibleItem.get();
    }

    public Property<CardPaneItemModel> visibleItemProperty() {
        return visibleItem;
    }

    public void setVisibleItem(CardPaneItemModel visibleItem) {
        this.visibleItem.set(visibleItem);
    }
}
