package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import org.controlsfx.control.SegmentedButton;
import to.remove.ui.SegmentedButtonModel;

public class SegmentedButtonFactory implements ProjectorNodeFactory<SegmentedButtonModel, SegmentedButton> {
    @Override
    public SegmentedButton create(Projector projector, SegmentedButtonModel itemModel) {
            SegmentedButton segmentedButton = new SegmentedButton();
            segmentedButton.getToggleGroup().selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
                if (newVal == null && oldVal != null)
                    oldVal.setSelected(true);
            });
            FXBinder.bind(segmentedButton.getButtons()).to(itemModel.getItems(), projector::createNode);
            return segmentedButton;
        }

    @Override
    public Class<SegmentedButtonModel> getSupportedType() {
        return SegmentedButtonModel.class;
    }
}
