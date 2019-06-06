package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.ButtonModel;
import javafx.scene.control.Button;

public class ButtonFactory extends ButtonBaseFactory<ButtonModel, Button> {

    @Override
    public Button create(final Projector projector, final ButtonModel model) {
        return createButtonBase(projector, model, new Button());
    }

    @Override
    public Class<ButtonModel> getSupportedType() {
        return ButtonModel.class;
    }
}
