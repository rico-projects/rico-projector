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

public class ImageViewModel extends ItemModel {
    private Property<String> resourcePath;
    private Property<Double> fitWidth;
    private Property<Double> fitHeight;
    private Property<Boolean> preserveRatio;

    public String getResourcePath() {
        return resourcePath.get();
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath.set(resourcePath);
    }

    public Property<String> resourcePathProperty() {
        return resourcePath;
    }

    public Double getFitWidth() {
        return fitWidth.get();
    }

    public void setFitWidth(Double fitWidth) {
        this.fitWidth.set(fitWidth);
    }

    public Property<Double> fitWidthProperty() {
        return fitWidth;
    }

    public Double getFitHeight() {
        return fitHeight.get();
    }

    public void setFitHeight(Double fitHeight) {
        this.fitHeight.set(fitHeight);
    }

    public Property<Double> fitHeightProperty() {
        return fitHeight;
    }

    public Boolean getPreserveRatio() {
        return preserveRatio.get();
    }

    public void setPreserveRatio(Boolean preserveRatio) {
        this.preserveRatio.set(preserveRatio);
    }

    public Property<Boolean> preserveRatioProperty() {
        return preserveRatio;
    }
}
