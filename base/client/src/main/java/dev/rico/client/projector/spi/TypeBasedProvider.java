package dev.rico.client.projector.spi;

public interface TypeBasedProvider<T> {

    Class<T> getSupportedType();
}
