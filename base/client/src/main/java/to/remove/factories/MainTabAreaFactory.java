package to.remove.factories;

import com.panemu.tiwulfx.control.DetachableTabPane;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import to.remove.ui.MainTabAreaModel;

public class MainTabAreaFactory implements ProjectorNodeFactory<MainTabAreaModel, Node> {

    @Override
    public Node create(final Projector projector, final MainTabAreaModel model) {
        DetachableTabPane tabPane = new DetachableTabPane();
        tabPane.setScope("scope1");
        tabPane.setStageOwnerFactory(parentStage -> null);
        tabPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Platform.runLater(() -> ((Stage) tabPane.getScene().getWindow().getScene().getWindow()).setMaximized(true));
        return new StackPane(tabPane);
    }

    @Override
    public Class<MainTabAreaModel> getSupportedType() {
        return MainTabAreaModel.class;
    }
}
