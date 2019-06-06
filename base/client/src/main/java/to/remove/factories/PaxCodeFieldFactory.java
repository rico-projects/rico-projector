package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.core.functional.Binding;
import dev.rico.internal.core.Assert;
import to.remove.ui.PaxCodeFieldModel;
import to.remove.uimanager.PaxCodeField;

import static dev.rico.client.remoting.FXWrapper.wrapStringProperty;
import static dev.rico.internal.client.projector.uimanager.TextField.configureTextInputControl;

public class PaxCodeFieldFactory implements ProjectorNodeFactory<PaxCodeFieldModel, PaxCodeField> {

    @Override
    public PaxCodeField create(final Projector projector, final PaxCodeFieldModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final PaxCodeField field = new PaxCodeField();
        final Binding textPropertyBinding = configureTextInputControl(projector.getControllerProxy(), model, field);
        textPropertyBinding.unbind();
        field.textProperty().bindBidirectional(wrapStringProperty(model.textProperty()));
        return field;
    }

    @Override
    public Class<PaxCodeFieldModel> getSupportedType() {
        return PaxCodeFieldModel.class;
    }
}
