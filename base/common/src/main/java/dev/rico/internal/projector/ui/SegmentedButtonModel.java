package dev.rico.internal.projector.ui;

import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class SegmentedButtonModel extends ItemListContainerModel<ToggleButtonModel> {
    private ObservableList<ToggleButtonModel> items;

    @Override
    public ObservableList<ToggleButtonModel> getItems() {
        return items;
    }
}