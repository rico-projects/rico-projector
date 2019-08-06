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
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.dialog.InfoDialogModel;
import javafx.scene.control.Alert;

public class InfoDialogHandler implements ProjectorDialogHandler<InfoDialogModel>, DialogConfiguration {

    @Override
    public void show(final Projector projector, final InfoDialogModel model) {
        Assert.requireNonNull(model, "model");
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        configureDialog(projector, alert, model);
        alert.setHeaderText(model.getHeaderText());
        alert.setContentText(model.getContentText());
        alert.showAndWait();
    }

    @Override
    public Class<InfoDialogModel> getSupportedType() {
        return InfoDialogModel.class;
    }
}
