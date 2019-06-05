package to.remove.ui.breadcrumbbar;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;

@ForRemoval
public class BreadCrumbBarModel extends ItemListContainerModel<BreadCrumbItemModel> {
    private Property<String> caption;
    private ObservableList<BreadCrumbItemModel> items;
    private Property<BreadCrumbItemModel> selected;

    @Override
    public ObservableList<BreadCrumbItemModel> getItems() {
        return items;
    }

    public BreadCrumbItemModel getSelected() {
        return selected.get();
    }

    public void setSelected(BreadCrumbItemModel selected) {
        this.selected.set(selected);
    }

    public Property<BreadCrumbItemModel> selectedProperty() {
        return selected;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }
}
