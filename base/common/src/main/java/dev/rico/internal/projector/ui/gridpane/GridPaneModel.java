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
package dev.rico.internal.projector.ui.gridpane;

import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public class GridPaneModel extends ItemListContainerModel<GridPaneItemModel> {
    private ObservableList<GridPaneItemModel> items;
    private ObservableList<GridPaneColumnConstraintsModel> columnConstraints;
    private Property<Double> hGap;
    private Property<Double> vGap;

    public Double gethGap() {
        return hGap.get();
    }

    public void sethGap(Double hGap) {
        this.hGap.set(hGap);
    }

    public Property<Double> hGapProperty() {
        return hGap;
    }

    public Double getvGap() {
        return vGap.get();
    }

    public void setvGap(Double vGap) {
        this.vGap.set(vGap);
    }

    public Property<Double> vGapProperty() {
        return vGap;
    }

    @Override
    public ObservableList<GridPaneItemModel> getItems() {
        return items;
    }

    public ObservableList<GridPaneColumnConstraintsModel> getColumnConstraints() {
        return columnConstraints;
    }
}
