package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.core.functional.Binding;
import dev.rico.internal.core.Assert;
import to.remove.ui.FuelFieldModel;
import to.remove.uimanager.FuelField;

import static dev.rico.client.remoting.FXWrapper.wrapStringProperty;
import static dev.rico.internal.client.projector.uimanager.TextField.configureTextInputControl;

public class FuelFieldFactory implements ProjectorNodeFactory<FuelFieldModel, FuelField> {

    @Override
    public FuelField create(final Projector projector, final FuelFieldModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final FuelField field = new FuelField(model);
        final Binding textPropertyBinding = configureTextInputControl(projector.getControllerProxy(), model, field);
        textPropertyBinding.unbind();
        field.textProperty().bindBidirectional(wrapStringProperty(model.textProperty()));
        return field;
    }

    @Override
    public Class<FuelFieldModel> getSupportedType() {
        return FuelFieldModel.class;
    }
}
