package dev.xhyrom.lighteco.common.manager;

import java.util.Collection;

public interface Manager<I, T> {
    T apply(I identifier);

    Collection<I> keys();
    Collection<T> values();

    T getOrMake(I identifier);

    T getIfLoaded(I identifier);

    boolean isLoaded(I identifier);

    void unload(I identifier);

    void unload(Collection<I> identifiers);
}
