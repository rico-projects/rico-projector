package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.ButtonModel;
import javafx.scene.control.Hyperlink;

public class HyperlinkFactory extends ButtonBaseFactory<ButtonModel, Hyperlink> {

    @Override
    public Hyperlink create(final Projector projector, final ButtonModel model) {
        return createButtonBase(projector, model, new Hyperlink());
    }

    @Override
    public Class<ButtonModel> getSupportedType() {
        return ButtonModel.class;
    }
}
