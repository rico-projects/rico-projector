package dev.rico.common.projector.ui.box;


import dev.rico.common.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Pos;

@RemotingBean
public class HBoxModel extends ItemListContainerModel<HBoxItemModel>  {
    private ObservableList<HBoxItemModel> items;
    private Property<Integer> spacing;
    private Property<Pos> alignment;

    public Integer getSpacing() {
        return spacing.get();
    }

    public void setSpacing(Integer spacing) {
        this.spacing.set(spacing);
    }

    public Property<Integer> spacingProperty() {
        return spacing;
    }

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
    public ObservableList<HBoxItemModel> getItems() {
        return items;
    }
}
