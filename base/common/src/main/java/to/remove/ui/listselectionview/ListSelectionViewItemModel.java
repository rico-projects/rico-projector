package to.remove.ui.listselectionview;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ListSelectionViewItemModel extends IdentifiableModel {
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

    @Override
    public String toString() {
        return getCaption();
    }
}
