package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import to.remove.ui.propertysheet.PropertySheetModel;
import to.remove.uimanager.PropertySheet;

public class PropertySheetFactory implements ProjectorNodeFactory<PropertySheetModel, PropertySheet> {

    @Override
    public PropertySheet create(final Projector projector, final PropertySheetModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        return new PropertySheet(projector.getControllerProxy(), model);
    }

    @Override
    public Class<PropertySheetModel> getSupportedType() {
        return PropertySheetModel.class;
    }
}
