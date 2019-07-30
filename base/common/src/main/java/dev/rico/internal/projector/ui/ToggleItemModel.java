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
package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ToggleItemModel extends ItemModel {
    private Property<String> caption;
    private Property<ItemModel> graphic;
    private Property<Boolean> selected;
    private Property<String> action;

    public Boolean getSelected() {
        return selected.get() != null && selected.get();
    }

    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    public Property<Boolean> selectedProperty() {
        return selected;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public ItemModel getGraphic() {
        return graphic.get();
    }

    public void setGraphic(ItemModel graphic) {
        this.graphic.set(graphic);
    }

    public Property<ItemModel> graphicProperty() {
        return graphic;
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public Property<String> actionProperty() {
        return action;
    }

}
