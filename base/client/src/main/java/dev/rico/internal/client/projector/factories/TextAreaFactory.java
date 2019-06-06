package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.internal.client.projector.uimanager.TextArea;

public class TextAreaFactory implements ProjectorNodeFactory<TextAreaModel, TextArea> {

    @Override
    public TextArea create(final Projector projector, final TextAreaModel model) {
        Assert.requireNonNull(projector, "projector");
        return new TextArea(projector.getControllerProxy(), model);
    }

    @Override
    public Class<TextAreaModel> getSupportedType() {
        return TextAreaModel.class;
    }
}
