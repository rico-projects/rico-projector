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
package dev.rico.internal.server.projector;

import java.util.function.Consumer;

import dev.rico.internal.projector.ui.IdentifiableModel;

public interface Retained<T extends IdentifiableModel> {

    T get();

    default boolean isPresent() {
        return get() != null;
    }

    default void ifPresent(final Consumer<T> consumer) {
        consumer.accept(get());
    }

    default void notPresent(final Runnable consumer) {
        if (get() == null) {
            consumer.run();
        }
    }

    static <T extends IdentifiableModel> Retained<T> empty() {
        return new Keeper<>();
    }

}
