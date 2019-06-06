package dev.rico.internal.client.projector.factories;

import com.google.common.base.Strings;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ToggleButtonModel;
import javafx.scene.control.ToggleButton;

import static dev.rico.client.remoting.FXBinder.bind;

public class ToggleButtonFactory implements ProjectorNodeFactory<ToggleButtonModel, ToggleButton> {

    @Override
    public ToggleButton create(final Projector projector, final ToggleButtonModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");
        
        final ToggleButton button = new ToggleButton();
        bind(button.graphicProperty()).to(model.graphicProperty(), projector::createNode);
        bind(button.selectedProperty()).bidirectionalTo(model.selectedProperty());
        bind(button.textProperty()).to(model.captionProperty());
        button.setOnAction(event -> {
            event.consume();
            if (Strings.isNullOrEmpty(model.getAction())) {
                projector.getControllerProxy().invoke("onToggleButtonAction", new Param("model", model), new Param("selected", button.isSelected())).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable));
            } else {
                projector.getControllerProxy().invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(button, throwable));
            }
        });
        return button;
    }

    @Override
    public Class<ToggleButtonModel> getSupportedType() {
        return ToggleButtonModel.class;
    }
}
