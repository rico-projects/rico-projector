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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UnexpectedErrorDialog extends Alert {

    private final TextArea textArea;
    private final ButtonType shutDownButton;
    private final BooleanProperty lethalCause = new SimpleBooleanProperty();

    public UnexpectedErrorDialog() {
        super(AlertType.ERROR);

        final Label label = new Label(ErrorMessageHandler.getInstance().getStacktraceMessage());

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        final GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        shutDownButton = new ButtonType(ErrorMessageHandler.getInstance().getQuiteApplicationText());

        getDialogPane().setExpandableContent(expContent);

        errorTextProperty().addListener(this::onErrorTextChange);

        setOnHidden(event -> {
            if (lethalCause.get() || getResult() == shutDownButton) {
                System.exit(0);
            }
        });
    }

    static public Void showError(final Node owner, final Throwable exception) {
        final UnexpectedErrorDialog unexpectedErrorDialog = new UnexpectedErrorDialog();
        if (owner != null && owner.getScene() != null && owner.getScene().getWindow() != null) {
            unexpectedErrorDialog.initOwner(owner.getScene().getWindow());
        }
        unexpectedErrorDialog.setStackTrace(exception);
        unexpectedErrorDialog.showAndWait();
        return null;
    }

    final public StringProperty errorTextProperty() {
        return textArea.textProperty();
    }

    public void setStackTrace(final Throwable stackTrace) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        stackTrace.printStackTrace(pw);
        final String exceptionText = sw.toString();
        errorTextProperty().setValue(exceptionText);
    }

    private void onErrorTextChange(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
        lethalCause.setValue(newValue.startsWith("java.lang.IllegalStateException: Connection is broken"));
        if (lethalCause.get()) {
            configureAsLethal();
        } else {
            configureAsRecoverable();
        }
    }

    private void configureAsLethal() {
        getButtonTypes().remove(shutDownButton);
        setTitle(ErrorMessageHandler.getInstance().getErrorTitle());
        setHeaderText(ErrorMessageHandler.getInstance().getErrorMessage());
    }

    private void configureAsRecoverable() {
        getButtonTypes().add(0, shutDownButton);
        setTitle(ErrorMessageHandler.getInstance().getErrorTitle());
        setHeaderText(ErrorMessageHandler.getInstance().getErrorMessage());
    }
}
