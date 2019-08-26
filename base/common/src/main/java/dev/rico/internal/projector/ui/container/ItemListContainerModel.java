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
package dev.rico.internal.projector.ui.container;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.WithPadding;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


/**
 * Ein Container der Item-Listen verwaltet.
 */
@RemotingBean
public abstract class ItemListContainerModel<ITEM_TYPE> extends ItemModel implements WithPadding {
    private Property<Integer> padding;

    public Integer getPadding() {
        return padding.get();
    }

    public void setPadding(Integer padding) {
        this.padding.set(padding);
    }

    public Property<Integer> paddingProperty() {
        return padding;
    }

    public void add(ITEM_TYPE itemModel) {
        getItems().add(itemModel);
    }

    public abstract ObservableList<ITEM_TYPE> getItems();

    @SafeVarargs
    public final void addAll(ITEM_TYPE... itemModel) {
        getItems().addAll(itemModel);
    }

    public void clear() {
        getItems().clear();
    }
}
