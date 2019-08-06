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
package dev.rico.internal.projector.ui.box;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import javafx.scene.layout.Priority;

@RemotingBean
public class VBoxItemModel extends IdentifiableModel {
    private Property<ItemModel> item;
    private Property<Priority> vGrow;

    public ItemModel getItem() {
        return item.get();
    }

    public void setItem(ItemModel itemModel) {
        this.item.set(itemModel);
    }

    public Property<ItemModel> itemProperty() {
        return item;
    }

    public Priority getvGrow() {
        return vGrow.get();
    }

    public void setvGrow(Priority vGrow) {
        this.vGrow.set(vGrow);
    }

    public Property<Priority> vGrowProperty() {
        return vGrow;
    }
}
