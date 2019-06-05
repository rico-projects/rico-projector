package to.remove.uimanager.breadcrumbbar;

import dev.rico.internal.projector.ForRemoval;
import javafx.scene.control.Skin;
import org.controlsfx.control.SegmentedButton;

@ForRemoval
public class BreadCrumbBar extends SegmentedButton {
    @Override
    protected Skin<?> createDefaultSkin() {
        return new BreadCrumbBarSkin(this);
    }
}