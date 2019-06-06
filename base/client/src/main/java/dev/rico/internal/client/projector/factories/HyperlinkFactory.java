package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.HyperlinkModel;
import javafx.scene.control.Hyperlink;

public class HyperlinkFactory extends ButtonBaseFactory<HyperlinkModel, Hyperlink> {

    @Override
    public Hyperlink create(final Projector projector, final HyperlinkModel model) {
        return createButtonBase(projector, model, new Hyperlink());
    }

    @Override
    public Class<HyperlinkModel> getSupportedType() {
        return HyperlinkModel.class;
    }
}
