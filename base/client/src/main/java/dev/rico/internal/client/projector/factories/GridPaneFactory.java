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
import dev.rico.internal.projector.ui.gridpane.GridPaneColumnConstraintsModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneItemModel;
import dev.rico.internal.projector.ui.gridpane.GridPaneModel;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Optional;

import static dev.rico.client.remoting.FXBinder.bind;

public class GridPaneFactory implements ProjectorNodeFactory<GridPaneModel, GridPane> {

    @Override
    public GridPane create(final Projector projector, final GridPaneModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");
        final GridPane gridPane = new GridPane();
        bind(gridPane.hgapProperty()).to(model.hGapProperty(), value -> getValue(value, gridPane::getHgap));
        bind(gridPane.vgapProperty()).to(model.vGapProperty(), value -> getValue(value, gridPane::getVgap));
        bind(gridPane.getChildren()).to(model.getItems(), content -> bindChildNode(projector, content));
        bind(gridPane.getColumnConstraints()).to(model.getColumnConstraints(), this::bindConstraint);
        return gridPane;
    }

    private ColumnConstraints bindConstraint(final GridPaneColumnConstraintsModel model) {
        final ColumnConstraints constraints = new ColumnConstraints();
        Optional.ofNullable(model.getFillWidth()).ifPresent(constraints::setFillWidth);
        Optional.ofNullable(model.getHalignment()).ifPresent(constraints::setHalignment);
        Optional.ofNullable(model.getHgrow()).ifPresent(constraints::setHgrow);
        Optional.ofNullable(model.getMaxWidth()).ifPresent(constraints::setMaxWidth);
        Optional.ofNullable(model.getMinWidth()).ifPresent(constraints::setMinWidth);
        Optional.ofNullable(model.getPrefWidth()).ifPresent(constraints::setPrefWidth);
        Optional.ofNullable(model.getPercentWidth()).ifPresent(constraints::setPercentWidth);
        return constraints;
    }

    @Override
    public Class<GridPaneModel> getSupportedType() {
        return GridPaneModel.class;
    }

    private Node bindChildNode(final Projector projector, final GridPaneItemModel model) {
        final Node child = projector.createNode(model.getItem());
        Optional.ofNullable(model.getRow()).ifPresent(value -> GridPane.setRowIndex(child, value));
        Optional.ofNullable(model.getCol()).ifPresent(value -> GridPane.setColumnIndex(child, value));
        Optional.ofNullable(model.getRowSpan()).ifPresent(value -> GridPane.setRowSpan(child, value));
        Optional.ofNullable(model.getColSpan()).ifPresent(value -> GridPane.setColumnSpan(child, value));
        Optional.ofNullable(model.gethAlignment()).ifPresent(value -> GridPane.setHalignment(child, value));
        Optional.ofNullable(model.getvAlignment()).ifPresent(value -> GridPane.setValignment(child, value));
        Optional.ofNullable(model.gethGrow()).ifPresent(value -> GridPane.setHgrow(child, value));
        Optional.ofNullable(model.getvGrow()).ifPresent(value -> GridPane.setVgrow(child, value));
        return child;
    }
}
