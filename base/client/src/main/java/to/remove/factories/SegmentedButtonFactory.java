package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.internal.core.Assert;
import org.controlsfx.control.SegmentedButton;
import to.remove.ui.SegmentedButtonModel;

public class SegmentedButtonFactory implements ProjectorNodeFactory<SegmentedButtonModel, SegmentedButton> {
    @Override
    public SegmentedButton create(final Projector projector, final SegmentedButtonModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getToggleGroup().selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null && oldVal != null)
                oldVal.setSelected(true);
        });
        FXBinder.bind(segmentedButton.getButtons()).to(model.getItems(), projector::createNode);
        return segmentedButton;
    }

    @Override
    public Class<SegmentedButtonModel> getSupportedType() {
        return SegmentedButtonModel.class;
    }
}
