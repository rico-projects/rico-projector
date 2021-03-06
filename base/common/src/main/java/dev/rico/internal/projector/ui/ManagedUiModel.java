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


import dev.rico.internal.projector.ui.dialog.DialogModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ManagedUiModel {

    private Property<ItemModel> root;
    private Property<DialogModel> dialog;
    private Property<Boolean> isWorking;
    private ObservableList<IdentifiableModel> retainedModels;
    private Property<Integer> badgeCount;

    public ItemModel getRoot() {
        return root.get();
    }

    public void setRoot(ItemModel itemModel) {
        root.set(itemModel);
    }

    public Property<ItemModel> rootProperty() {
        return root;
    }

    public Boolean getIsWorking() {
        return isWorking.get() != null && isWorking.get();
    }

    public void setIsWorking(Boolean isWorking) {
        this.isWorking.set(isWorking);
    }

    public Property<Boolean> isWorkingProperty() {
        return isWorking;
    }

    public void showDialog(DialogModel dialog) {
        this.dialog.set(dialog);
    }

    public Property<DialogModel> dialogProperty() {
        return dialog;
    }

    public ObservableList<IdentifiableModel> getRetainedModels() {
        return retainedModels;
    }

    public Integer getBadgeCount() {
        return badgeCount.get();
    }

    public void setBadgeCount(Integer badgeCount) {
        this.badgeCount.set(badgeCount);
    }

    public Property<Integer> badgeCountProperty() {
        return badgeCount;
    }

}
