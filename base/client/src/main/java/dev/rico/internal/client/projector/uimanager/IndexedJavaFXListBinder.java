/*
 * Copyright 2015-2017 Canoo Engineering AG.
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
package dev.rico.internal.client.projector.uimanager;

import dev.rico.core.functional.Binding;
import dev.rico.core.functional.Subscription;
import dev.rico.internal.core.Assert;
import dev.rico.remoting.ListChangeEvent;
import dev.rico.remoting.ObservableList;
import javafx.collections.ListChangeListener;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;

public class IndexedJavaFXListBinder<S> /*implements JavaFXListBinder<S>*/ {

    private static IdentityHashMap<javafx.collections.ObservableList, javafx.collections.ObservableList> boundLists = new IdentityHashMap<>();
    private javafx.collections.ObservableList<S> list;

    public IndexedJavaFXListBinder(javafx.collections.ObservableList<S> list) {
        Assert.requireNonNull(list, "list");
        this.list = list;
    }

    public <T> Binding to(ObservableList<T> dolphinList, Function<ConversionInfo<T>, ? extends S> converter) {
        Assert.requireNonNull(dolphinList, "dolphinList");
        Assert.requireNonNull(converter, "converter");
        if (boundLists.containsKey(list)) {
            throw new UnsupportedOperationException("A JavaFX list can only be bound to one Dolphin Platform list!");
        }

        boundLists.put(list, list);
        final InternalListChangeListener<T> listChangeListener = new InternalListChangeListener<>(converter);
        final Subscription subscription = dolphinList.onChanged(listChangeListener);

        List<S> newPart = new ArrayList<>();
        for (int i = 0; i < dolphinList.size(); i++) {
            T item = dolphinList.get(i);
            newPart.add(converter.apply(new ConversionInfo<>(i, item)));
        }
        list.setAll(newPart);

//        ListChangeListener<S> readOnlyListener = c -> {
//            if (!listChangeListener.onChange) {
//                throw new UnsupportedOperationException("A JavaFX list that is bound to a dolphin list can only be modified by the binding!");
//            }
//        };
//        list.addListener(readOnlyListener);

        return () -> {
            subscription.unsubscribe();
//            list.removeListener(readOnlyListener);
            boundLists.remove(list);
        };
    }

    public <T> Binding bidirectionalTo(ObservableList<T> dolphinList, Function<ConversionInfo<T>, ? extends S> converter, Function<ConversionInfo<S>, ? extends T> backConverter) {
        Assert.requireNonNull(dolphinList, "dolphinList");
        Assert.requireNonNull(converter, "converter");
        Assert.requireNonNull(backConverter, "backConverter");
        if (boundLists.containsKey(list)) {
            throw new IllegalStateException("A JavaFX list can only be bound to one Dolphin Platform list!");
        }

        final InternalBidirectionalListChangeListener<T> listChangeListener = new InternalBidirectionalListChangeListener<>(dolphinList, converter, backConverter);
        final Subscription subscription = dolphinList.onChanged(listChangeListener);

        List<S> newPart = new ArrayList<>();
        for (int i = 0; i < dolphinList.size(); i++) {
            T item = dolphinList.get(i);
            newPart.add(converter.apply(new ConversionInfo<>(i, item)));
        }
        list.setAll(newPart);

        list.addListener(listChangeListener);

        return () -> {
            subscription.unsubscribe();
            list.removeListener(listChangeListener);
        };
    }

    private class InternalListChangeListener<T> implements dev.rico.remoting.ListChangeListener<T> {

        private final Function<ConversionInfo<T>, ? extends S> converter;

        protected boolean onChange;

        private InternalListChangeListener(Function<ConversionInfo<T>, ? extends S> converter) {
            this.converter = converter;
            onChange = false;
        }

        @Override
        public void listChanged(ListChangeEvent<? extends T> e) {
            if (onChange) {
                return;
            }

            onChange = true;
            try {
                for (ListChangeEvent.Change<? extends T> c : e.getChanges()) {
                    final int index = c.getFrom();
                    if (c.isRemoved() || c.isReplaced()) {
                        list.subList(index, index + c.getRemovedElements().size()).clear();
                    }
                    if (c.isAdded() || c.isReplaced()) {
                        List<S> newPart = new ArrayList<>();
                        List<? extends T> subList = e.getSource().subList(index, c.getTo());
                        for (int i = 0; i < subList.size(); i++) {
                            T item = subList.get(i);
                            newPart.add(converter.apply(new ConversionInfo<>(index + i, item)));
                        }
                        list.addAll(index, newPart);
                    }
                }
            } finally {
                onChange = false;
            }
        }
    }

    public static class ConversionInfo<T> {
        private final int index;
        private final T input;

        public ConversionInfo(int index, T input) {
            this.index = index;
            this.input = input;
        }

        public int getIndex() {
            return index;
        }

        public T getInput() {
            return input;
        }
    }

    private class InternalBidirectionalListChangeListener<T> extends InternalListChangeListener<T> implements ListChangeListener<S> {

        private final ObservableList<T> dolphinList;
        private final Function<ConversionInfo<S>, ? extends T> backConverter;

        private InternalBidirectionalListChangeListener(ObservableList<T> dolphinList,
                                                        Function<ConversionInfo<T>, ? extends S> converter,
                                                        Function<ConversionInfo<S>, ? extends T> backConverter) {
            super(converter);
            this.dolphinList = dolphinList;
            this.backConverter = backConverter;
        }

        @Override
        public void onChanged(Change<? extends S> change) {
            if (onChange) {
                return;
            }

            onChange = true;
            try {
                while (change.next()) {
                    // TODO: Replace with subList() operation once implemented
                    final int index = change.getFrom();
                    if (change.wasRemoved() || change.wasReplaced()) {
                        for (int i = 0; i < change.getRemovedSize(); i++) {
                            dolphinList.remove(index);
                        }
                    }
                    if (change.wasAdded() || change.wasReplaced()) {
                        List<T> newPart = new ArrayList<>();
                        List<? extends S> subList = change.getAddedSubList();
                        for (int i = 0; i < subList.size(); i++) {
                            S item = subList.get(i);
                            newPart.add(backConverter.apply(new ConversionInfo<>(index + i, item)));
                        }
                        dolphinList.addAll(index, newPart);
                    }
                }
            } finally {
                onChange = false;
            }
        }
    }
}
