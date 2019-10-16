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
import dev.rico.internal.projector.ui.ToolBarModel;
import javafx.geometry.Insets;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;

import static dev.rico.client.remoting.FXBinder.bind;

public class ToolBarFactory implements ProjectorNodeFactory<ToolBarModel, ToolBar> {

    @Override
    public ToolBar create(final Projector projector, final ToolBarModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        final ToolBar toolBar = new ToolBar() {
            @Override
            public void resize(double width, double height) {
                super.resize(width, height);
                System.out.println("TOOLBAR HEIGHT: " + height);
            }

            @Override
            protected double computePrefHeight(double width) {
                return super.computePrefHeight(width);
            }
        };
        toolBar.setPadding(new Insets(15));
        toolBar.setMinHeight(Region.USE_PREF_SIZE);
        toolBar.setMaxHeight(Region.USE_PREF_SIZE);
        bind(toolBar.getItems()).to(model.getItems(), projector::createNode);
        return toolBar;
    }

    @Override
    public Class<ToolBarModel> getSupportedType() {
        return ToolBarModel.class;
    }
}
