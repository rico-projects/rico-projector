package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.CheckBoxModel;
import javafx.scene.control.CheckBox;

import static dev.rico.client.remoting.FXBinder.bind;

public class CheckBoxFactory implements ProjectorNodeFactory<CheckBoxModel, CheckBox>, ActionHandlerFactory {

    @Override
    public CheckBox create(final Projector projector, final CheckBoxModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final CheckBox checkBox = new CheckBox();
        bind(checkBox.selectedProperty()).bidirectionalTo(model.selectedProperty());
        bind(checkBox.textProperty()).to(model.captionProperty());
        checkBox.setOnAction(createOnActionHandler("buttonClick", model, projector));
        return checkBox;
    }

    @Override
    public Class<CheckBoxModel> getSupportedType() {
        return CheckBoxModel.class;
    }
}
