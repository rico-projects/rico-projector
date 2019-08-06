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
package dev.rico.projector.extension;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

import java.util.Optional;

@RemotingBean
public class SliderModel extends ItemModel {

    private Property<Double> min;

    private Property<Double> max;

    private Property<Double> value;

    public Property<Double> minProperty() {
        return min;
    }

    public SliderModel withMin(final double min) {
        setMin(min);
        return this;
    }

    public double getMin() {
        return Optional.ofNullable(minProperty().get()).orElse(-1d);
    }

    public void setMin(final double min) {
        minProperty().set(min);
    }

    public Property<Double> maxProperty() {
        return max;
    }

    public SliderModel withMax(final double max) {
        setMax(max);
        return this;
    }

    public double getMax() {
        return Optional.ofNullable(maxProperty().get()).orElse(-1d);
    }

    public void setMax(final double max) {
        maxProperty().set(max);
    }

    public Property<Double> valueProperty() {
        return value;
    }

    public SliderModel withValue(final double value) {
        setValue(value);
        return this;
    }

    public double getValue() {
        return Optional.ofNullable(valueProperty().get()).orElse(-1d);
    }

    public void setValue(final double value) {
        valueProperty().set(value);
    }
}
