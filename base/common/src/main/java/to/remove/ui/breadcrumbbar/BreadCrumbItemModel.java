package to.remove.ui.breadcrumbbar;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;

@ForRemoval
public class BreadCrumbItemModel extends IdentifiableModel {
    private Property<String> caption;

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
