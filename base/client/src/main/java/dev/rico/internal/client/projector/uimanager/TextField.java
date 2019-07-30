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

import dev.rico.internal.projector.ui.TextFieldModel;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.core.functional.Binding;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;

import java.util.Optional;

import static dev.rico.client.remoting.FXBinder.bind;

public class TextField extends javafx.scene.control.TextField {
    public TextField(ControllerProxy<?> controllerProxy, TextFieldModel model) {
        bind(prefColumnCountProperty()).to(model.prefColumnCountProperty());
        configureTextInputControl(controllerProxy, model, this);
    }

    public static Binding configureTextInputControl(ControllerProxy<?> controllerProxy, TextFieldModel model, TextInputControl control) {
        bind(control.promptTextProperty()).bidirectionalTo(model.promptTextProperty());
        bind(control.editableProperty()).to(model.editableProperty(), value -> value == null ? true : value);
        if (model.getAction() != null) {
            final PauseTransition pause = new PauseTransition(Duration.millis(Optional.ofNullable(model.getActionDelay()).orElse(850)));
            control.textProperty().addListener((observable, oldValue, newValue) -> {
                pause.setOnFinished(event -> controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(control, throwable)));
                pause.playFromStart();
            });
        }
        return bind(control.textProperty()).bidirectionalTo(model.textProperty());
    }
}
