package dev.rico.client.projector.spi;

import dev.rico.client.remoting.BidirectionalConverter;
import dev.rico.client.remoting.Converter;
import dev.rico.internal.client.projector.uimanager.table.ManagedTableView;
import dev.rico.internal.core.Assert;

import java.util.function.Supplier;

public interface DefaultValueAccess {
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

    default <T> Converter<T, T> defaultValue(Supplier<T> fallbackValueGetter) {
        return value -> getValue(value, fallbackValueGetter);
    }

    default <T> ManagedTableView.BidirectionalConverterWithFallback defaultValueBidirectional(Supplier<T> fallbackValueGetter) {
        return new BidirectionalConverterWithFallback<>(fallbackValueGetter);
    }

    class BidirectionalConverterWithFallback<T> implements BidirectionalConverter<T, T> {
        private final Supplier<T> fallbackValueGetter;

        BidirectionalConverterWithFallback(Supplier<T> fallbackValueGetter) {
            this.fallbackValueGetter = fallbackValueGetter;
        }

        @Override
        public T convertBack(T value) {
            return value == null ? fallbackValueGetter.get() : value;
        }

        @Override
        public T convert(T value) {
            return value == null ? fallbackValueGetter.get() : value;
        }
    }
}
