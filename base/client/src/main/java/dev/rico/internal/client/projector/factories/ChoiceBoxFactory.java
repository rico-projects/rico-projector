package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.client.projector.uimanager.ManagedChoiceBox;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import javafx.scene.control.ChoiceBox;

public class ChoiceBoxFactory implements ProjectorNodeFactory<ChoiceBoxModel, ChoiceBox<ChoiceBoxItemModel>> {

    @Override
    public ChoiceBox<ChoiceBoxItemModel> create(final Projector projector, final ChoiceBoxModel model) {
        Assert.requireNonNull(projector, "projector");

        return new ManagedChoiceBox(model, projector.getControllerProxy());
    }

    @Override
    public Class<ChoiceBoxModel> getSupportedType() {
        return ChoiceBoxModel.class;
    }
}
