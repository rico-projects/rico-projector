package dev.rico.sample;

import dev.rico.client.remoting.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

import static dev.rico.sample.SampleConstants.CONTROLLER_NAME;

public class SampleClient extends AbstractRemotingApplication {

    @Override
    protected URL getServerEndpoint() throws MalformedURLException {
        return new URL("http://localhost:8080/remoting");
    }

    @Override
    protected void start(final Stage primaryStage, final ClientContext clientContext) throws Exception {

        final Label demo = new Label("OFFLINE");

        clientContext.createController(CONTROLLER_NAME).handle((c, e) -> {
            if(e != null) {
                throw new RuntimeException("Error", e);
            }
            demo.setText("ONLINE");
            return null;
        });

        final Scene scene = new Scene(demo);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
