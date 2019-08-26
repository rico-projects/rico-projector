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
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxItemModel;
import dev.rico.internal.projector.ui.choicebox.ChoiceBoxModel;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

import static dev.rico.client.remoting.FXWrapper.wrapList;

public class ManagedChoiceBox extends ChoiceBox<ChoiceBoxItemModel> {

    public ManagedChoiceBox(final ChoiceBoxModel model, final ControllerProxy<? extends ManagedUiModel> controllerProxy) {

        setConverter(new StringConverter<ChoiceBoxItemModel>() {
            @Override
            public String toString(final ChoiceBoxItemModel object) {
                if (object == null) {
                    return null;
                }
                return object.getCaption();
            }

            @Override
            public ChoiceBoxItemModel fromString(final String string) {
                return null;
            }
        });

        setModel(model, controllerProxy);
    }

    public void setModel(final ChoiceBoxModel model, final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        setItems(wrapList(model.getItems()));
        ClientUiHelper.bindWithSelectionModel(model.selectedProperty(), getSelectionModel());
        if (model.getAction() != null) {
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                controllerProxy.invoke(model.getAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(this, throwable));
            });
        }
    }
}
