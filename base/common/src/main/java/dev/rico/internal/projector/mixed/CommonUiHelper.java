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
package dev.rico.internal.projector.mixed;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.remoting.collections.ListChangeEventImpl;
import dev.rico.remoting.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

public class CommonUiHelper {
    public static <T> void subscribe(Property<T> property, ValueChangeListener<T> changeListener) {
        changeListener.valueChanged(new ValueChangeEvent<T>() {
            @Override
            public Property<T> getSource() {
                return property;
            }

            @Override
            public T getOldValue() {
                return null;
            }

            @Override
            public T getNewValue() {
                return property.get();
            }
        });
        property.onChanged(changeListener);
    }

    public static <T> void subscribe(ObservableList<T> observableList, ListChangeListener<T> listener) {
        listener.listChanged(new ListChangeEventImpl<>(observableList, 0, observableList.size(), new ArrayList<>()));
        observableList.onChanged(listener);
    }

   public static <T> void subscribeWithOptional(Property<T> property, Consumer<Optional<T>> optionalValue) {
        subscribe(property, evt -> optionalValue.accept(Optional.ofNullable(evt.getNewValue())));
    }


   public static void setProperty(IdentifiableModel model, String property, String value) {
      java.util.Objects.requireNonNull(model);
      java.util.Objects.requireNonNull(property);
        model.getProperties().removeIf(s -> s.startsWith(property + "="));
      if (value != null) {
         model.getProperties().add(property + "=" + value);
      }
   }

   public static String getProperty(IdentifiableModel model, String property) {
      java.util.Objects.requireNonNull(model);
      java.util.Objects.requireNonNull(property);
      String result = model.getProperties().stream().filter(s -> s.startsWith(property + "="))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find property: " + property));
      if (result.length() >= property.length() + 1) {
         result = result.substring(property.length() + 1);
      }
      return result;
   }

   public static boolean hasProperty(IdentifiableModel model, String property) {
      java.util.Objects.requireNonNull(model);
      java.util.Objects.requireNonNull(property);
      String result = model.getProperties().stream().filter(s -> s.startsWith(property + "="))
            .findFirst().orElse(null);
      return result != null;
   }
}
