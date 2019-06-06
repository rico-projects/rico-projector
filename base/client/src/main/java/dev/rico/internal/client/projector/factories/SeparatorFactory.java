package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.SeparatorModel;
import javafx.scene.control.Separator;

import static dev.rico.client.remoting.FXBinder.bind;

public class SeparatorFactory implements ProjectorNodeFactory<SeparatorModel, Separator> {
    @Override
    public Separator create(Projector projector, SeparatorModel model) {
        Separator separator = new Separator();
        bind(separator.orientationProperty()).to(model.orientationProperty());
        return separator;
    }

    @Override
    public Class<SeparatorModel> getSupportedType() {
        return SeparatorModel.class;
    }
}
