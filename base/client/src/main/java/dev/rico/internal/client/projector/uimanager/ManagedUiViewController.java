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

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.view.AbstractViewController;
import dev.rico.internal.client.projector.mixed.ClientContextHolder;
import dev.rico.internal.client.projector.uimanager.presenter.ViewPresenter;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class ManagedUiViewController<M extends ManagedUiModel> extends AbstractViewController<M> implements ViewPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedUiViewController.class);

    private final BorderPane pane = new BorderPane();

    private ClientUiManager uiManager;

    public ManagedUiViewController(final String controllerName) {
        super(ClientContextHolder.getContext(), controllerName);
    }

    private static String stripEnding(final String clazz) {
        if (!clazz.endsWith("ViewPresenter")) {
            return clazz;
        } else {
            final int viewIndex = clazz.lastIndexOf("ViewPresenter");
            return clazz.substring(0, viewIndex);
        }
    }

    @Override
    protected void init() {
        try {
            FXBinder.bind(isWorkingProperty()).to(getModel().isWorkingProperty());
            addCSSIfAvailable(pane);
            uiManager = newClientUiManager();
            pane.centerProperty().bind(uiManager.rootProperty());
        } catch (final Exception e) {
            throw e;
        }
    }

    protected ClientUiManager newClientUiManager() {
        return new ClientUiManager(getControllerProxy(), newPostProcessor());
    }

    private void addCSSIfAvailable(final Parent parent) {
        final URL uri = getClass().getResource(getStyleSheetName());
        if (uri != null) {
            final String uriToCss = uri.toExternalForm();
            parent.getStylesheets().add(uriToCss);
        }
    }

    protected PostProcessor newPostProcessor() {
        return (projector, id, model, node) -> {
        };
    }

    private String getStyleSheetName() {
        return getResourceCamelOrLowerCase(false, ".css");
    }

    private String getResourceCamelOrLowerCase(final boolean mandatory, final String ending) {
        String name = getConventionalName(true, ending);
        URL found = getClass().getResource(name);
        if (found != null) {
            return name;
        } else {
            LOGGER.error("File: {} not found, attempting with camel case", name);
            name = getConventionalName(false, ending);
            found = getClass().getResource(name);
            if (mandatory && found == null) {
                final String message = "Cannot load file " + name;
                LOGGER.error(message);
                LOGGER.error("Stopping initialization phase...");
                throw new IllegalStateException(message);
            } else {
                return name;
            }
        }
    }

    private String getConventionalName(final boolean lowercase, final String ending) {
        return getConventionalName(lowercase) + ending;
    }

    private String getConventionalName(final boolean lowercase) {
        final String clazzWithEnding = getClass().getSimpleName();
        String clazz = stripEnding(clazzWithEnding);
        if (lowercase) {
            clazz = clazz.toLowerCase();
        }

        return clazz;
    }

    @Override
    protected void onInitializationException(final Throwable throwable) {
        showError(throwable);
    }

    @Override
    protected void onInvocationException(final Throwable throwable) {
        showError(throwable);
    }

    @Override
    public Node getRootNode() {
        return pane;
    }

    protected Window getDialogOwner() {
        if (getRootNode() != null && getRootNode().getScene() != null && getRootNode().getScene().getWindow() != null) {
            return getRootNode().getScene().getWindow();
        }
        return null;
    }

    protected void showError(final Throwable throwable) {
        Platform.runLater(() -> {
            final UnexpectedErrorDialog unexpectedErrorDialog = new UnexpectedErrorDialog();
            unexpectedErrorDialog.setStackTrace(throwable);
            unexpectedErrorDialog.showAndWait();
        });
    }

    @Override
    public Node getView() {
        return getRootNode();
    }

    @Override
    public SimpleBooleanProperty isWorkingProperty() {
        final SimpleBooleanProperty result = new SimpleBooleanProperty(true);
        modelProperty().addListener(observable -> {
            result.set(getModel().getIsWorking());
            getModel().isWorkingProperty().onChanged(evt -> result.setValue(evt.getNewValue()));
        });
        return result;
    }
}
