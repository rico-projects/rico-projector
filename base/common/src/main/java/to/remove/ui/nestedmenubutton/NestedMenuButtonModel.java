package to.remove.ui.nestedmenubutton;

import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.internal.projector.ui.menuitem.MenuItemModel;
import dev.rico.remoting.ObservableList;

public class NestedMenuButtonModel extends ButtonModel {
    private ObservableList<MenuItemModel> items;

    public ObservableList<MenuItemModel> getItems() {
        return items;
    }
}
