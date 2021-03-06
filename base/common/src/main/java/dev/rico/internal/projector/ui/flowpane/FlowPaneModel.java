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
package dev.rico.internal.projector.ui.flowpane;


import dev.rico.internal.projector.ui.WithPadding;
import dev.rico.internal.projector.ui.container.ItemListContainerModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

@RemotingBean
public class FlowPaneModel extends ItemListContainerModel<FlowPaneItemModel> implements WithPadding {
    private Property<Orientation> orientation;
    private ObservableList<FlowPaneItemModel> items;
    private Property<Pos> alignment;
    private Property<Double> vGap;
    private Property<Double> hGap;

    public Pos getAlignment() {
        return alignment.get();
    }

    public void setAlignment(Pos alignment) {
        this.alignment.set(alignment);
    }

    public Property<Pos> alignmentProperty() {
        return alignment;
    }

    public Double getVgap() {
        return vGap.get();
    }

    public void setVgap(Double vgap) {
        this.vGap.set(vgap);
    }

    public Property<Double> vGapProperty() {
        return vGap;
    }

    public Double getHgap() {
        return hGap.get();
    }

    public void setHgap(Double hgap) {
        this.hGap.set(hgap);
    }

    public Property<Double> hGapProperty() {
        return hGap;
    }

    @Override
    public ObservableList<FlowPaneItemModel> getItems() {
        return items;
    }

    public Orientation getOrientation() {
        return orientation.get();
    }

    public Property<Orientation> orientationProperty() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }
}
