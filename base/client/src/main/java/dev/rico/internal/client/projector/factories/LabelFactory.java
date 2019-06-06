package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.projector.ui.LabelModel;
import javafx.scene.control.Label;

public class LabelFactory implements ProjectorNodeFactory<LabelModel, Label> {

    @Override
    public Label create(final Projector projector, final LabelModel model) {
        Label label = new Label(model.getText());
        bind(label.textProperty()).to(model.textProperty());
        bind(label.wrapTextProperty()).to(model.wrapTextProperty(), value -> getValue(value, true));
        bind(label.alignmentProperty()).to(model.alignmentProperty());
        bind(label.textAlignmentProperty()).to(model.textAlignmentProperty());
        return label;
    }

    @Override
    public Class<LabelModel> getSupportedType() {
        return LabelModel.class;
    }
}
