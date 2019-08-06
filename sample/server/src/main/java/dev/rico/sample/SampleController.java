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
package dev.rico.sample;

import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.LabelModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.TextAreaModel;
import dev.rico.internal.projector.ui.box.HBoxModel;
import dev.rico.internal.projector.ui.box.VBoxModel;
import dev.rico.internal.projector.ui.dialog.InfoDialogModel;
import dev.rico.internal.server.projector.AbstractManagedUiController;
import dev.rico.projector.extension.SliderModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.RemotingAction;
import dev.rico.server.remoting.RemotingContext;
import dev.rico.server.remoting.RemotingController;
import dev.rico.server.remoting.RemotingModel;

import static dev.rico.sample.SampleConstants.CONTROLLER_NAME;

@RemotingController(CONTROLLER_NAME)
public class SampleController extends AbstractManagedUiController {
    @RemotingModel
    private ManagedUiModel model;

    public SampleController(final BeanManager beanManager, final RemotingContext session) {
        super(beanManager, session);
    }

    @Override
    public ItemModel buildUi() {
        final LabelModel label = ui().label("Hello World");

        final TextAreaModel center = ui().textArea();
        center.setText("Rico rocks!");

        final ButtonModel button = ui().button("Don't press this button!");
        button.setAction("onDontPressButtonAction");

        final ButtonModel button2 = ui().button("Don't press this button!");
        button2.setAction("onDontPressButtonAction");

        final SliderModel slider = ui().create(SliderModel.class).withMin(10).withMax(20).withValue(12);
        slider.valueProperty().onChanged(e -> System.out.println("SLIDER -> " + slider.getValue()));

        final VBoxModel borderPane = ui().vBox(label, center, button, button2, slider);

        return borderPane;
    }

    @RemotingAction
    private void onDontPressButtonAction() {
        final InfoDialogModel dialog = ui().infoDialog(null);
        dialog.setHeaderText("I said not!");
        dialog.setContentText("You should not do this!!!");
        getModel().showDialog(dialog);
    }

    @Override
    public ManagedUiModel getModel() {
        return model;
    }
}
