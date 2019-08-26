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
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.client.remoting.FXBinder;
import dev.rico.internal.client.projector.uimanager.ActionEventEventHandler;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.dialog.CustomDialogModel;
import impl.org.controlsfx.skin.DecorationPane;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;

import java.util.Optional;

public class CustomDialogHandler implements ProjectorDialogHandler<CustomDialogModel>, DialogConfiguration {

    @Override
    public void show(final Projector projector, final CustomDialogModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");
        final Dialog<Boolean> dialog = new Dialog<>();
        dialog.setResizable(true);
        final Optional<Node> nodeOptional = Optional.ofNullable(model.getOwner()).map(projector.getModelToNodeMap()::get);
        final Optional<Window> windowOptional = nodeOptional.flatMap(node -> Optional.of(node).map(Node::getScene).map(Scene::getWindow));
        windowOptional.ifPresent(dialog::initOwner);
        dialog.setTitle(model.getTitle());
        dialog.setHeaderText(model.getHeaderText());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Node content = projector.createNode(model.getContent());
        final DecorationPane decorationPane = new DecorationPane();
        decorationPane.setRoot(content);
        dialog.getDialogPane().setContent(decorationPane);
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        FXBinder.bind(okButton.disableProperty()).to(model.okayEnabledProperty(), aBoolean -> aBoolean != null && !aBoolean);
        dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK);
        if (model.getCheckAction() != null) {
            okButton.addEventFilter(ActionEvent.ACTION, new ActionEventEventHandler(projector.getControllerProxy(), model, okButton));
        }
        dialog.showAndWait().ifPresent(aBoolean -> {
            if (aBoolean) {
                projector.getControllerProxy().invoke(model.getOkayAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(nodeOptional.orElse(null), throwable));
            }
        });
    }

    @Override
    public Class<CustomDialogModel> getSupportedType() {
        return CustomDialogModel.class;
    }
}
