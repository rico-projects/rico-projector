package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.PasswordFieldModel;
import dev.rico.internal.projector.ui.flowpane.FlowPaneModel;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.FlowPane;

import static dev.rico.client.remoting.FXBinder.bind;
import static dev.rico.internal.client.projector.uimanager.TextField.configureTextInputControl;

public class PasswordFieldFactory implements ProjectorNodeFactory<PasswordFieldModel, PasswordField> {

    @Override
    public PasswordField create(final Projector projector, final PasswordFieldModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final PasswordField result = new PasswordField();
        bind(result.prefColumnCountProperty()).to(model.prefColumnCountProperty());
        configureTextInputControl(projector.getControllerProxy(), model, result);
        return result;
    }

    @Override
    public Class<PasswordFieldModel> getSupportedType() {
        return PasswordFieldModel.class;
    }
}
