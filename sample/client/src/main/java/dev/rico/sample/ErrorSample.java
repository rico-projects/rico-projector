package dev.rico.sample;

import dev.rico.client.FxToolkit;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.JavaFXProjectorImpl;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ErrorSample extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {

    }

    public static void main(String[] args) {
        launch();
    }
}
