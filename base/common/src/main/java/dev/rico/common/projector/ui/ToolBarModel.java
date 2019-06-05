package dev.rico.common.projector.ui;

import dev.rico.remoting.ObservableList;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public class ToolBarModel extends ItemModel {
    private ObservableList<ItemModel> items;

    public ObservableList<ItemModel> getItems() {
        return items;
    }
}
