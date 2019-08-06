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
package dev.rico.projector.model;

import dev.rico.remoting.Property;

public interface WithText<M extends WithText> {

    Property<String> textProperty();

    default String getText() {
        return textProperty().get();
    }

    default void setText(final String text) {
        textProperty().set(text);
    }

    default M withText(final String text) {
        setText(text);
        return (M) this;
    }
}
