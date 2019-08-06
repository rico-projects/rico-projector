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


import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class ListViewItemModel extends IdentifiableModel {
    private ObservableList<String> content;
    private Property<String> title;
    private Property<String> detail1;
    private Property<String> detail2;

    public String getDetail2() {
        return detail2.get();
    }

    public void setDetail2(String detail2) {
        this.detail2.set(detail2);
    }

    public Property<String> detail2Property() {
        return detail2;
    }

    public ObservableList<String> getContent() {
        return content;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Property<String> titleProperty() {
        return title;
    }

    public String getDetail1() {
        return detail1.get();
    }

    public void setDetail1(String detail1) {
        this.detail1.set(detail1);
    }

    public Property<String> detail1Property() {
        return detail1;
    }
}
