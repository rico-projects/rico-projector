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
package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;

import java.util.Optional;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.TextFieldModel;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;

public class TextFieldFactory implements ProjectorNodeFactory<TextFieldModel, TextField> {

    @Override
    public TextField create(final Projector projector, final TextFieldModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final TextField textField = new TextField();
        bind(textField.prefColumnCountProperty()).to(model.prefColumnCountProperty());
        bind(textField.promptTextProperty()).bidirectionalTo(model.promptTextProperty());
        bind(textField.editableProperty()).to(model.editableProperty(), value -> getValue(value, true));
        bind(textField.textProperty()).bidirectionalTo(model.textProperty());
        configureTextInputControl(projector.getControllerProxy(), model, textField);
        return textField;
    }

    @Override
    public Class<TextFieldModel> getSupportedType() {
        return  TextFieldModel.class;
    }


    private void configureTextInputControl(final ControllerProxy<?> controllerProxy, final TextFieldModel model, final TextInputControl control) {
        Assert.requireNonNull(controllerProxy, "controllerProxy");
        Assert.requireNonNull(model, "model");
        Assert.requireNonNull(control, "control");

        if (model.getAction() != null) {
            final PauseTransition pause = getPauseTransition(model);
            control.textProperty().addListener((observable, oldValue, newValue) -> {
                pause.setOnFinished(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog
                        .showError(control, throwable)));
                pause.playFromStart();
            });
        }
    }

    private PauseTransition getPauseTransition(final TextFieldModel model) {
        Assert.requireNonNull(model, "model");
        return new PauseTransition(
                Duration.millis(Optional.ofNullable(model.getActionDelay()).orElse(850)));
    }
}
