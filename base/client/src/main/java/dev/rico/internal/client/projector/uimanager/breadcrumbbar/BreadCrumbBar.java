package dev.rico.internal.client.projector.uimanager.breadcrumbbar;

import javafx.scene.control.Skin;
import org.controlsfx.control.SegmentedButton;

public class BreadCrumbBar extends SegmentedButton {
    @Override
    protected Skin<?> createDefaultSkin() {
        return new BreadCrumbBarSkin(this);
    }
}