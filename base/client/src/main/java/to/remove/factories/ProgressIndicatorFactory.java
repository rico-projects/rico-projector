package to.remove.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import to.remove.ui.ProgressIndicatorModel;
import to.remove.uimanager.ProgressIndicator;

public class ProgressIndicatorFactory implements ProjectorNodeFactory<ProgressIndicatorModel, ProgressIndicator> {

    @Override
    public ProgressIndicator create(final Projector projector, final ProgressIndicatorModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ProgressIndicator progressIndicator = new ProgressIndicator();
        bind(model.waitingProperty()).to(progressIndicator.activeProperty());
        return progressIndicator;
    }

    @Override
    public Class<ProgressIndicatorModel> getSupportedType() {
        return ProgressIndicatorModel.class;
    }
}
