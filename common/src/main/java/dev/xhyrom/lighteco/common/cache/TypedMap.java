package dev.xhyrom.lighteco.common.cache;

import java.util.HashMap;
import java.util.Map;

public class TypedMap<T> {
    private final Map<T, Object> map = new HashMap<>();

    public <V> V get(T key) {
        return (V) map.get(key);
    }

    public <V> V getOrDefault(T key, V defaultValue) {
        return (V) map.getOrDefault(key, defaultValue);
    }

    public <V> void put(T key, V value) {
        map.put(key, value);
    }

    public void clear() {
        map.clear();
    }
}
