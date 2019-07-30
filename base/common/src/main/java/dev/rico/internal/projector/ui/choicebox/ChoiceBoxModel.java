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
package dev.rico.internal.projector.ui.choicebox;


import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ChoiceBoxModel extends ItemModel {
    private ObservableList<ChoiceBoxItemModel> items;
    private Property<ChoiceBoxItemModel> selected;
    private Property<String> action;

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

    public ObservableList<ChoiceBoxItemModel> getItems() {
        return items;
    }

    public ChoiceBoxItemModel getSelected() {
        return selected.get();
    }

    public Property<ChoiceBoxItemModel> selectedProperty() {
        return selected;
    }

    public void setSelected(ChoiceBoxItemModel selected) {
        this.selected.set(selected);
    }
}
