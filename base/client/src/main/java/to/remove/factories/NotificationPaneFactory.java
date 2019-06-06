package to.remove.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import org.controlsfx.control.NotificationPane;
import to.remove.ui.NotificationPaneModel;

import static dev.rico.client.remoting.FXBinder.bind;

public class NotificationPaneFactory implements ProjectorNodeFactory<NotificationPaneModel, NotificationPane> {

    @Override
    public NotificationPane create(final Projector projector, final NotificationPaneModel model) {
        NotificationPane pane = new NotificationPane();
        bind(pane.contentProperty()).to(model.contentProperty(), projector::createNode);
        bind(pane.textProperty()).to(model.textProperty());
        model.textProperty().onChanged(evt -> {
            if (evt.getNewValue() == null || evt.getNewValue().isEmpty()) {
                pane.hide();
            } else {
                pane.show();
            }
        });
        return pane;
    }

    @Override
    public Class<NotificationPaneModel> getSupportedType() {
        return NotificationPaneModel.class;
    }
}
