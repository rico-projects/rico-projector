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
package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.util.Optional;

public interface DialogConfiguration {

    default Optional<Node> configureDialog(Projector projector, Alert alert, DialogModel model) {
        Optional<Node> nodeOptional = Optional.ofNullable(model.getOwner()).map(projector.getModelToNodeMap()::get);
        Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(alert::initOwner);
        alert.setTitle(model.getTitle());
        return nodeOptional;
    }
}
