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
package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.JavaFXProjectorImpl;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

import java.util.List;

public class ClientUiManager {

    private final JavaFXProjectorImpl projector;

    public ClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        projector = newProjector(controllerProxy);
    }

    protected JavaFXProjectorImpl newProjector(ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        return new JavaFXProjectorImpl(controllerProxy);
    }

    public Node createNode(final ItemModel itemModel) {
        return projector.createNode(itemModel);
    }

    public ObjectProperty<Node> rootProperty() {
        return projector.rootProperty();
    }

    public List<PostProcessor> getPostProcessors(){
        return projector.getPostProcessors();
    }
}
