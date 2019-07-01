package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.Converter;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ImageViewModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static dev.rico.client.remoting.FXBinder.bind;

public class ImageViewFactory implements ProjectorNodeFactory<ImageViewModel, ImageView> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageViewFactory.class);

    @Override
    public ImageView create(final Projector projector, final ImageViewModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ImageView imageView = new ImageView();

        final Converter<String, Image> imageConverter = v -> getImageForPath(v);
        bind(imageView.imageProperty()).to(model.resourcePathProperty(), imageConverter);
        bind(imageView.preserveRatioProperty()).to(model.preserveRatioProperty(), value -> getValue(value, true));
        bind(imageView.fitWidthProperty()).to(model.fitWidthProperty());
        bind(imageView.fitHeightProperty()).to(model.fitHeightProperty());

        return imageView;
    }

    private Image getImageForPath(final String v) {
        if (v == null) {
            return null;
        } else {
            final URL resource = Image.class.getResource(v.substring("classpath:".length()));
            if (resource == null) {
                LOG.warn("Image '{}' not found!", v);
                return null;
            }
            return new Image(resource.toExternalForm());
        }
    }

    @Override
    public Class<ImageViewModel> getSupportedType() {
        return ImageViewModel.class;
    }
}
