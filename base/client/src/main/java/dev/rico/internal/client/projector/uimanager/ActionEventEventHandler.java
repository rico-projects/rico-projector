package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.projector.ui.dialog.CustomDialogModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ActionEventEventHandler implements EventHandler<ActionEvent> {
    private final CustomDialogModel newDialog;
    private final Button okButton;
    private boolean invokeAction = true;
    private final Object lock = new Object();

    private final ControllerProxy controllerProxy;

    public ActionEventEventHandler(ControllerProxy controllerProxy, CustomDialogModel newDialog, Button okButton) {
        this.controllerProxy = controllerProxy;
        this.newDialog = newDialog;
        this.okButton = okButton;
    }

    @Override
    public void handle(ActionEvent event) {
        synchronized (lock) {
            if (invokeAction) {
                invokeAction = false;
                event.consume();
                Thread thread = new Thread(() -> {
                    CompletableFuture<Void> future = controllerProxy.invoke(newDialog.getCheckAction());
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        synchronized (lock) {
                            if (newDialog.getCheckSuccessful() != null && newDialog.getCheckSuccessful()) {
                                okButton.fire();
                            } else
                                invokeAction = true;
                        }
                    });
                });
                thread.start();
            }
        }
    }
}
