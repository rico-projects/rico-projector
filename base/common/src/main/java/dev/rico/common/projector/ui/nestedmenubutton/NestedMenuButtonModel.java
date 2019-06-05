package dev.rico.common.projector.ui.nestedmenubutton;

import dev.rico.common.projector.ui.ButtonModel;
import dev.rico.common.projector.ui.menuitem.MenuItemModel;
import dev.rico.remoting.ObservableList;

public class NestedMenuButtonModel extends ButtonModel {
    private ObservableList<MenuItemModel> items;

    public ObservableList<MenuItemModel> getItems() {
        return items;
    }
}
