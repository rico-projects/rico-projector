package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.LabelModel;
import javafx.scene.control.Label;

import static dev.rico.client.remoting.FXBinder.bind;

public class LabelFactory implements ProjectorNodeFactory<LabelModel, Label> {

    @Override
    public Label create(final Projector projector, final LabelModel model) {
        Assert.requireNonNull(model, "model");

        final Label label = new Label(model.getText());
        bind(label.textProperty()).to(model.textProperty());
        bind(label.wrapTextProperty()).to(model.wrapTextProperty(), value -> getValue(value, label::isWrapText));
        bind(label.alignmentProperty()).to(model.alignmentProperty(), value -> getValue(value, label::getAlignment));
        bind(label.textAlignmentProperty()).to(model.textAlignmentProperty(), value -> getValue(value, label::getTextAlignment));
        return label;
    }

    @Override
    public Class<LabelModel> getSupportedType() {
        return LabelModel.class;
    }
}
