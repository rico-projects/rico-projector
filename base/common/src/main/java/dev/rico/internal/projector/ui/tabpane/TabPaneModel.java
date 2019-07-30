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
package dev.rico.internal.projector.ui.tabpane;


import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Side;

@RemotingBean
public class TabPaneModel extends ItemListContainerModel<TabPaneItemModel> {
    private ObservableList<TabPaneItemModel> items;
    private Property<Side> sideProperty;

    @Override
    public ObservableList<TabPaneItemModel> getItems() {
        return items;
    }

    public Property<Side> sideProperty() {
        return sideProperty;
    }

    public void setSide(Side side) {
        sideProperty.set(side);
    }

    public Side getSide() {
        return sideProperty.get();
    }
}
