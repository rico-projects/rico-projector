package dev.rico.projector.extension;


import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.internal.core.Assert;
import javafx.scene.control.Slider;

public class SliderFactory implements ProjectorNodeFactory<SliderModel, Slider> {

    @Override
    public Slider create(final Projector projector, final SliderModel model) {
        Assert.requireNonNull(model, "model");

        final Slider slider = new Slider();
        FXBinder.bind(slider.valueProperty()).bidirectionalToNumeric(model.valueProperty());
        FXBinder.bind(slider.minProperty()).bidirectionalToNumeric(model.minProperty());
        FXBinder.bind(slider.maxProperty()).bidirectionalToNumeric(model.maxProperty());

        return slider;
    }

    @Override
    public Class getSupportedType() {
        return SliderModel.class;
    }
}
