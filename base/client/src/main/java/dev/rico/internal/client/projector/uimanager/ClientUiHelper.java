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

import dev.rico.internal.core.Assert;
import dev.rico.remoting.Property;
import javafx.scene.control.SelectionModel;

public class ClientUiHelper {

    private ClientUiHelper() {
    }

    public static <T> void bindWithSelectionModel(final Property<T> selectedProperty, final SelectionModel<T> selectionModel) {
        Assert.requireNonNull(selectedProperty, "selectedProperty");
        Assert.requireNonNull(selectionModel, "selectionModel");
        selectionModel.select(selectedProperty.get());
        selectedProperty.onChanged(evt -> {
            if (evt.getNewValue() == null && !selectionModel.isEmpty()) {
                selectionModel.clearSelection();
            } else if (evt.getNewValue() != null && !evt.getNewValue().equals(selectionModel.getSelectedItem())) {
                selectionModel.select(evt.getNewValue());
            }
        });
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedProperty.set(newValue));
    }
}
