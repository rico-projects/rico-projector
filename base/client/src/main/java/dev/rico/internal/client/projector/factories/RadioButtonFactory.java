package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.RadioButtonModel;
import javafx.scene.control.RadioButton;

import java.util.WeakHashMap;

import static dev.rico.client.remoting.FXBinder.bind;

public class RadioButtonFactory implements ProjectorNodeFactory<RadioButtonModel, RadioButton>, ActionHandlerFactory {

    @Override
    public RadioButton create(final Projector projector, final RadioButtonModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final RadioButton button = new RadioButton();
        bind(button.selectedProperty()).bidirectionalTo(model.selectedProperty());
        bind(button.textProperty()).to(model.captionProperty());
        button.setOnAction(createOnActionHandler("buttonClick", model, projector));
        return button;
    }

    @Override
    public Class<RadioButtonModel> getSupportedType() {
        return RadioButtonModel.class;
    }
}
