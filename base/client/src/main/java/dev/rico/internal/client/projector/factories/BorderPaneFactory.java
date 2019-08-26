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
import dev.rico.internal.projector.ui.BorderPaneModel;
import dev.rico.internal.projector.ui.ItemModel;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class BorderPaneFactory implements ProjectorNodeFactory<BorderPaneModel, BorderPane> {
    @Override
    public BorderPane create(final Projector projector, final BorderPaneModel itemModel) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(itemModel, "itemModel");
        final Callback<ItemModel, Node> convert = param -> {
            if (param == null) {
                return null;
            }
            return projector.createNode(param);
        };
        final BorderPane pane = new BorderPane();

        pane.setTop(convert.call(itemModel.getTop()));
        pane.setLeft(convert.call(itemModel.getLeft()));
        pane.setRight(convert.call(itemModel.getRight()));
        pane.setBottom(convert.call(itemModel.getBottom()));
        pane.setCenter(convert.call(itemModel.getCenter()));

        itemModel.topProperty().onChanged(evt -> pane.setTop(convert.call(evt.getNewValue())));
        itemModel.leftProperty().onChanged(evt -> pane.setLeft(convert.call(evt.getNewValue())));
        itemModel.rightProperty().onChanged(evt -> pane.setRight(convert.call(evt.getNewValue())));
        itemModel.bottomProperty().onChanged(evt -> pane.setBottom(convert.call(evt.getNewValue())));
        itemModel.centerProperty().onChanged(evt -> pane.setCenter(convert.call(evt.getNewValue())));

        return pane;
    }

    @Override
    public Class<BorderPaneModel> getSupportedType() {
        return BorderPaneModel.class;
    }
}
