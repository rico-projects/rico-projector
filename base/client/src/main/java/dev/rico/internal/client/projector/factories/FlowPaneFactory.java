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

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.flowpane.FlowPaneModel;
import javafx.scene.layout.FlowPane;

public class FlowPaneFactory implements ProjectorNodeFactory<FlowPaneModel, FlowPane> {

    @Override
    public FlowPane create(final Projector projector, final FlowPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final FlowPane pane = new FlowPane();
        bind(pane.vgapProperty()).to(model.vGapProperty());
        bind(pane.hgapProperty()).to(model.hGapProperty());
        bind(pane.orientationProperty()).to(model.orientationProperty());
        bind(pane.alignmentProperty()).to(model.alignmentProperty());
        bind(pane.getChildren()).to(model.getItems(), content -> projector.createNode(content.getItem()));
        return pane;
    }

    @Override
    public Class<FlowPaneModel> getSupportedType() {
        return FlowPaneModel.class;
    }
}
