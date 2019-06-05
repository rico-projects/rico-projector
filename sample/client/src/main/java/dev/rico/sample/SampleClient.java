package dev.rico.sample;

import dev.rico.client.remoting.AbstractRemotingApplication;
import dev.rico.client.remoting.ClientContext;
import dev.rico.internal.client.projector.mixed.ClientContextHolder;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        ClientContextHolder.setContext(clientContext);

        SampleViewPresenter presenter = new SampleViewPresenter(CONTROLLER_NAME);

        final Scene scene = new Scene((Parent) presenter.getView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
