package dev.xhyrom.lighteco.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class ExpiringSet<T> {
    private final Cache<T, Long> cache;
    private final long lifetime;

    public ExpiringSet(long duration, TimeUnit unit) {
        this.cache = CacheBuilder.newBuilder()
            .expireAfterWrite(duration, unit)
            .build();
        this.lifetime = unit.toMillis(duration);
    }

    public boolean add(T item) {
        boolean present = contains(item);
        this.cache.put(item, System.currentTimeMillis() + this.lifetime);
        return !present;
    }

    public boolean contains(T item) {
        Long timeout = this.cache.getIfPresent(item);
        return timeout != null && timeout > System.currentTimeMillis();
    }

    public void remove(T item) {
        this.cache.invalidate(item);
    }
}
