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

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.Param;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.CheckBoxModel;
import javafx.scene.control.CheckBox;

import static dev.rico.client.remoting.FXBinder.bind;

public class CheckBoxFactory implements ProjectorNodeFactory<CheckBoxModel, CheckBox>, ActionHandlerFactory {

    @Override
    public CheckBox create(final Projector projector, final CheckBoxModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final CheckBox checkBox = new CheckBox();
        bind(checkBox.selectedProperty()).bidirectionalTo(model.selectedProperty());
        bind(checkBox.textProperty()).to(model.captionProperty());
        if (model.getAction() != null) {
            checkBox.setOnAction(createOnActionHandler(projector, model.getAction(), checkBox, new Param("source", model)));
        }
        return checkBox;
    }

    @Override
    public Class<CheckBoxModel> getSupportedType() {
        return CheckBoxModel.class;
    }
}
