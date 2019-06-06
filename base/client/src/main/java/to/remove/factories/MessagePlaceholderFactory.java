package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import to.remove.ui.MessagePlaceholder;

public class MessagePlaceholderFactory implements ProjectorNodeFactory<MessagePlaceholder, Label> {

    @Override
    public Label create(final Projector projector, final MessagePlaceholder model) {
        final Label label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setTextFill(Color.RED);
        label.setPadding(new Insets(0, 0, 0, 10));
        label.setWrapText(true);
        return label;
    }

    @Override
    public Class<MessagePlaceholder> getSupportedType() {
        return MessagePlaceholder.class;
    }
}
