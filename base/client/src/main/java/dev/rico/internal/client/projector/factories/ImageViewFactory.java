package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.Converter;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ImageViewModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static dev.rico.client.remoting.FXBinder.bind;

public class ImageViewFactory implements ProjectorNodeFactory<ImageViewModel, ImageView> {

    @Override
    public ImageView create(final Projector projector, final ImageViewModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ImageView imageView = new ImageView();

        final Converter<String, Image> imageConverter = v -> v == null ? null : new Image(Image.class.getResource(v.substring("classpath:".length())).toExternalForm());
        bind(imageView.imageProperty()).to(model.resourcePathProperty(), imageConverter);
        bind(imageView.preserveRatioProperty()).to(model.preserveRatioProperty(), value -> value == null ? true : value);
        bind(imageView.fitWidthProperty()).to(model.fitWidthProperty());
        bind(imageView.fitHeightProperty()).to(model.fitHeightProperty());

        return imageView;
    }

    @Override
    public Class<ImageViewModel> getSupportedType() {
        return ImageViewModel.class;
    }
}
