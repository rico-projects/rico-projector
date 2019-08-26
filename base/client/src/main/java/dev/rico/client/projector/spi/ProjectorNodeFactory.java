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
package dev.rico.client.projector.spi;

import dev.rico.client.projector.Projector;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import javafx.scene.Node;

import java.util.function.Supplier;

public interface ProjectorNodeFactory<M extends IdentifiableModel, N extends Node> extends TypeBasedProvider<M> {

    N create(Projector projector, M model);

    default <S> S getValue(S value, S fallbackValue) {
        Assert.requireNonNull(fallbackValue, "fallbackValue");
        if (value == null) {
            return fallbackValue;
        } else {
            return value;
        }
    }

    default <S> S getValue(final S fromBinding, final Supplier<S> fallbackGetter) {
        Assert.requireNonNull(fallbackGetter, "fallbackGetter");
        return fromBinding == null ? fallbackGetter.get() : fromBinding;
    }
}
