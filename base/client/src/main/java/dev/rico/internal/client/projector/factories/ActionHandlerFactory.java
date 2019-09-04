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
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ActionHandlerFactory {

    default EventHandler<ActionEvent> createOnActionHandler(final Projector projector, final String action, final IdentifiableModel identifiableModel, Param... args) {
        Assert.requireNonNull(projector, "projector");
        final Map<IdentifiableModel, Node> modelToNodeMap = projector.getModelToNodeMap();
        Assert.requireNonNull(modelToNodeMap, "modelToNodeMap");
        return createOnActionHandler(projector, action, modelToNodeMap.get(identifiableModel), args);
    }

    default EventHandler<ActionEvent> createOnActionHandler(final Projector projector, final String action, final Node node, Param... args) {
        Assert.requireNonNull(action, "action");
        Assert.requireNonNull(node, "node");
        Assert.requireNonNull(projector, "projector");
        final ControllerProxy<? extends ManagedUiModel> controllerProxy = projector.getControllerProxy();
        Assert.requireNonNull(controllerProxy, "controllerProxy");
        return event -> {
            event.consume();
            final CompletableFuture<Void> typeInvocation = controllerProxy.invoke(action, args);
            Assert.requireNonNull(typeInvocation, "typeInvocation");
            typeInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(node, throwable));
        };
    }
}
