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
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.Param;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.mixed.CommonUiHelper;
import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.remoting.Property;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tooltip;

import java.util.function.Consumer;

import static dev.rico.client.remoting.FXBinder.bind;

public abstract class ButtonBaseFactory<T extends ButtonModel, S extends ButtonBase> implements ProjectorNodeFactory<T, S>, ActionHandlerFactory {

    private static <T> void subscribe(final Property<T> property, final Consumer<T> consumer) {
        CommonUiHelper.subscribe(property, evt -> consumer.accept(evt.getNewValue()));
    }

    protected S createButtonBase(final Projector projector, final T model, final S node) {
        configureButton(projector, model, node);
        if (model.getAction() != null) {
            node.setOnAction(createOnActionHandler(projector, model.getAction(), node, new Param("button", model)));
        }
        return node;
    }

    protected void configureButton(final Projector projector, final T model, final S node) {
        Assert.requireNonNull(model, "model");
        Assert.requireNonNull(node, "node");

        bind(node.textProperty()).to(model.captionProperty());
        subscribe(model.tooltipProperty(), tooltipOptional -> createTooltip(tooltipOptional, node));
        FXBinder.bind(node.graphicProperty()).to(model.graphicProperty(), projector::createNode);
    }

    private void createTooltip(final String tooltipText, final S node) {
        if (tooltipText != null) {
            final Tooltip tooltip = new Tooltip(tooltipText);
            node.setTooltip(tooltip);
        } else {
            node.setTooltip(null);
        }
    }
}
