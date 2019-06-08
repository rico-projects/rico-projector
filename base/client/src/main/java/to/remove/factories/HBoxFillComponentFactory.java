package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import to.remove.components.HBoxFillComponent;
import to.remove.ui.HBoxFillComponentModel;

public class HBoxFillComponentFactory implements ProjectorNodeFactory<HBoxFillComponentModel, HBoxFillComponent> {

    @Override
    public HBoxFillComponent create(final Projector projector, final HBoxFillComponentModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        return new HBoxFillComponent();
    }

    @Override
    public Class<HBoxFillComponentModel> getSupportedType() {
        return HBoxFillComponentModel.class;
    }
}
