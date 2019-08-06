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
import dev.rico.internal.projector.ui.ScrollPaneModel;
import javafx.scene.control.ScrollPane;

public class ScrollPaneFactory implements ProjectorNodeFactory<ScrollPaneModel, ScrollPane> {

    @Override
    public ScrollPane create(final Projector projector, final ScrollPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        bind(scrollPane.contentProperty()).to(model.contentProperty(), projector::createNode);
        return scrollPane;
    }

    @Override
    public Class<ScrollPaneModel> getSupportedType() {
        return ScrollPaneModel.class;
    }
}
