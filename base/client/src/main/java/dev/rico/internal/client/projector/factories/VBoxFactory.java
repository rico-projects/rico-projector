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
import dev.rico.internal.projector.ui.box.VBoxModel;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import static dev.rico.client.remoting.FXBinder.bind;

public class VBoxFactory implements ProjectorNodeFactory<VBoxModel, VBox> {

    @Override
    public VBox create(final Projector projector, final VBoxModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final VBox vBox = new VBox();
        bind(vBox.spacingProperty()).to(model.spacingProperty(), value -> getValue(value, 0));
        bind(vBox.alignmentProperty()).to(model.alignmentProperty());
        bind(vBox.getChildren()).to(model.getItems(), content -> {
            final Node child = projector.createNode(content.getItem());
            VBox.setVgrow(child, content.getvGrow());
            return child;
        });
        return vBox;
    }

    @Override
    public Class<VBoxModel> getSupportedType() {
        return VBoxModel.class;
    }
}
