package dev.rico.internal.projector.ui.gridpane;

import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public class GridPaneModel extends ItemListContainerModel<GridPaneItemModel> {
    private ObservableList<GridPaneItemModel> items;
    private Property<Double> hGap;
    private Property<Double> vGap;

    public Double gethGap() {
        return hGap.get();
    }

    public void sethGap(Double hGap) {
        this.hGap.set(hGap);
    }

    public Property<Double> hGapProperty() {
        return hGap;
    }

    public Double getvGap() {
        return vGap.get();
    }

    public void setvGap(Double vGap) {
        this.vGap.set(vGap);
    }

    public Property<Double> vGapProperty() {
        return vGap;
    }

    @Override
    public ObservableList<GridPaneItemModel> getItems() {
        return items;
    }
}
