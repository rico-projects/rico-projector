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
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.LabelModel;
import javafx.scene.control.Label;

import static dev.rico.client.remoting.FXBinder.bind;

public class LabelFactory implements ProjectorNodeFactory<LabelModel, Label> {

    @Override
    public Label create(final Projector projector, final LabelModel model) {
        Assert.requireNonNull(model, "model");

        final Label label = new Label(model.getText());
        bind(label.textProperty()).to(model.textProperty());
        bind(label.wrapTextProperty()).to(model.wrapTextProperty(), value -> getValue(value, label::isWrapText));
        bind(label.alignmentProperty()).to(model.alignmentProperty(), value -> getValue(value, label::getAlignment));
        bind(label.textAlignmentProperty()).to(model.textAlignmentProperty(), value -> getValue(value, label::getTextAlignment));
        return label;
    }

    @Override
    public Class<LabelModel> getSupportedType() {
        return LabelModel.class;
    }
}
