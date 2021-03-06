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
package dev.rico.internal.client.projector;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.projector.spi.TypeBasedProvider;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.WithPadding;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.*;
import java.util.function.Supplier;

import static dev.rico.client.remoting.FXBinder.bind;

public class JavaFXProjectorImpl implements Projector {

    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;

    private final List<PostProcessor> postProcessors;

    private final Map<Class<? extends ItemModel>, ProjectorNodeFactory> factories;

    private final Map<Class<? extends DialogModel>, ProjectorDialogHandler> dialogHandlers;

    private final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = new WeakHashMap<>();

    private final ObjectProperty<Node> root = new SimpleObjectProperty<>();

    public JavaFXProjectorImpl(final ControllerProxy<? extends ManagedUiModel> controllerProxy, List<PostProcessor> postProcessors) {
        this.controllerProxy = Assert.requireNonNull(controllerProxy, "controllerProxy");
        this.postProcessors = postProcessors;

        try {
            factories = loadServiceProviders(ProjectorNodeFactory.class);
            dialogHandlers = loadServiceProviders(ProjectorDialogHandler.class);
        } catch (final Exception e) {
            throw new RuntimeException("Error in loading ui component factories", e);
        }

        final ManagedUiModel model = controllerProxy.getModel();
        Assert.requireNonNull(model, "model");
        model.rootProperty().onChanged(evt -> updateUiRoot(evt.getNewValue()));
        updateUiRoot(model.getRoot());

        model.dialogProperty().onChanged(event -> Platform.runLater(() -> {
            final DialogModel newDialog = event.getNewValue();
            if (newDialog != null) {
                final ProjectorDialogHandler projectorDialogHandler = dialogHandlers.get(newDialog.getClass());
                if (projectorDialogHandler == null) {
                    throw new IllegalStateException("No handler found for dialog type:" + newDialog.getClass());
                } else {
                    projectorDialogHandler.show(this, newDialog);
                }
            }
        }));
    }

    private <T, S extends TypeBasedProvider<T>> Map<Class<? extends T>, S> loadServiceProviders(final Class<S> serviceClass) {
        Assert.requireNonNull(serviceClass, "serviceClass");
        final Map<Class<? extends T>, S> map = new HashMap<>();
        final ServiceLoader<S> serviceLoader = ServiceLoader.load(serviceClass);
        for (final S provider : serviceLoader) {
            final Class<? extends T> type = provider.getSupportedType();
            if (type == null) {
                throw new IllegalStateException("Supported type of " + serviceClass.getSimpleName() + " implementation '" + provider.getClass() + "' must not be 'null'");
            }
            if (map.containsKey(type)) {
                final String className = Optional.ofNullable(map.get(type)).map(f -> f.getClass().getSimpleName()).orElse("UNKNOWN");
                throw new IllegalStateException("Provider for type '" + type + "' is already defined by factory. See concrete factories " + provider.getClass().getSimpleName() + " and " + className);
            }
            map.put(type, provider);
        }
        return map;
    }

    private void updateUiRoot(final ItemModel itemModel) {
        root.set(createNode(itemModel));
    }

    @Override
    public Node getRoot() {
        return root.get();
    }

    public ObjectProperty<Node> rootProperty() {
        return root;
    }

    @Override
    public <N extends Node> N createNode(final ItemModel itemModel) {
        if (itemModel == null) return null;
        if (modelToNodeMap.containsKey(itemModel)) {
            return (N) modelToNodeMap.get(itemModel);
        }
        final ProjectorNodeFactory factory = factories.get(itemModel.getClass());
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for " + itemModel.getClass());
        }
        final N node = (N) factory.create(this, itemModel);
        modelToNodeMap.put(itemModel, node);
        bindDefaultProperties(node, itemModel);
        postProcess(node, itemModel);
        return node;
    }

    private <N extends Node> void bindDefaultProperties(final N node, final ItemModel<?> model) {
        bind(node.disableProperty()).to(model.disableProperty(), value -> fallback(value, node::isDisable));
        bind(node.visibleProperty()).to(model.visibleProperty(), value -> fallback(value, node::isVisible));
        bind(node.managedProperty()).to(model.managedProperty(), value -> fallback(value, node::isManaged));
        model.getStyleClass().addAll(node.getStyleClass());
        bind(node.styleProperty()).to(model.styleProperty());
        bind(node.getStyleClass()).bidirectionalTo(model.getStyleClass());

        if (node instanceof Region) {
            final Region region = (Region) node;
            bind(region.minWidthProperty()).to(model.minWidthProperty(), value -> fallback(value, region::getMinWidth));
            bind(region.minHeightProperty()).to(model.minHeightProperty(), value -> fallback(value, region::getMinHeight));
            bind(region.maxWidthProperty()).to(model.maxWidthProperty(), value -> fallback(value, region::getMaxWidth));
            bind(region.maxHeightProperty()).to(model.maxHeightProperty(), value -> fallback(value, region::getMaxHeight));
            bind(region.prefWidthProperty()).to(model.prefWidthProperty(), value -> fallback(value, region::getPrefWidth));
            bind(region.prefHeightProperty()).to(model.prefHeightProperty(), value -> fallback(value, region::getPrefHeight));
            if (model instanceof WithPadding) {
                WithPadding withPadding = (WithPadding) model;
                bind(region.paddingProperty()).to(withPadding.paddingProperty(), value -> value == null ? new Insets(0) : new Insets(withPadding.getPadding()));
            }
        }
    }

    private <T> T fallback(final T fromBinding, final Supplier<T> fallbackGetter) {
        return fromBinding == null ? fallbackGetter.get() : fromBinding;
    }

    private void postProcess(final Node node, final ItemModel itemModel) {
        Assert.requireNonNull(itemModel, "itemModel");
        itemModel.idProperty().onChanged(evt -> {
            final String newId = evt.getNewValue();
            postProcessors.forEach(processor -> processor.postProcess(this, newId, itemModel, node));
        });
        postProcessors.forEach(processor -> processor.postProcess(this, itemModel.getId(), itemModel, node));
    }

    @Override
    public ControllerProxy<? extends ManagedUiModel> getControllerProxy() {
        return controllerProxy;
    }

    @Override
    public Map<IdentifiableModel, Node> getModelToNodeMap() {
        return modelToNodeMap;
    }

    @Override
    public List<PostProcessor> getPostProcessors() {
        return postProcessors;
    }
}
