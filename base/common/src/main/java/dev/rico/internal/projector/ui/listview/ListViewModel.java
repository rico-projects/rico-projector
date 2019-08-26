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
package dev.rico.internal.projector.ui.listview;

import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.internal.projector.ui.menuitem.MenuItemModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;

public class ListViewModel extends ItemListContainerModel<ListViewItemModel> {
    private ObservableList<ListViewItemModel> items;
    private Property<ListViewItemModel> selected;
    private Property<String> selectedAction;
    private ObservableList<MenuItemModel> itemMenu;
    private Property<String> rendererClass;

    @Override
    public ObservableList<ListViewItemModel> getItems() {
        return items;
    }

    public String getRendererClass() {
        return rendererClass.get();
    }

    public void setRendererClass(String rendererClass) {
        this.rendererClass.set(rendererClass);
    }

    public Property<String> rendererClassProperty() {
        return rendererClass;
    }

    public ListViewItemModel getSelected() {
        return selected.get();
    }

    public void setSelected(ListViewItemModel selected) {
        this.selected.set(selected);
    }

    public Property<ListViewItemModel> selectedProperty() {
        return selected;
    }

    public ObservableList<MenuItemModel> getItemMenu() {
        return itemMenu;
    }

    public String getSelectedAction() {
        return selectedAction.get();
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction.set(selectedAction);
    }

    public Property<String> selectedActionProperty() {
        return selectedAction;
    }
}
