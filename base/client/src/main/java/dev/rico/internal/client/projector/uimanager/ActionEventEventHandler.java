/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019 The original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.dialog.CustomDialogModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ActionEventEventHandler implements EventHandler<ActionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ActionEventEventHandler.class);

    private final CustomDialogModel newDialog;
    private final Button okButton;
    private final Object lock = new Object();
    private final ControllerProxy controllerProxy;
    private boolean invokeAction = true;

    public ActionEventEventHandler(final ControllerProxy controllerProxy, final CustomDialogModel newDialog, final Button okButton) {
        this.controllerProxy = Assert.requireNonNull(controllerProxy, "controllerProxy");
        this.newDialog = Assert.requireNonNull(newDialog, "newDialog");
        this.okButton = Assert.requireNonNull(okButton, "okButton");
    }

    @Override
    public void handle(final ActionEvent event) {
        synchronized (lock) {
            if (invokeAction) {
                invokeAction = false;
                event.consume();
                final Thread thread = new Thread(() -> {
                    final CompletableFuture<Void> future = controllerProxy.invoke(newDialog.getCheckAction());
                    try {
                        future.get();
                    } catch (final InterruptedException | ExecutionException e) {
                        LOG.error("Error", e);
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
