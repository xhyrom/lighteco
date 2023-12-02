package dev.xhyrom.lighteco.common.messaging;

import dev.xhyrom.lighteco.api.messenger.MessengerProvider;
import dev.xhyrom.lighteco.common.messaging.type.redis.RedisMessengerProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.Set;

public class MessagingFactory {
    private final LightEcoPlugin plugin;

    public MessagingFactory(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    protected LightEcoPlugin getPlugin() {
        return this.plugin;
    }

    public Set<MessagingType> getRequiredTypes() {
        return Set.of(this.plugin.getConfig().messaging.provider);
    }

    public MessagingService get() {
        MessagingType provider = this.plugin.getConfig().messaging.provider;
        return new LightEcoMessagingService(this.plugin, createProvider(provider));
    }

    private MessengerProvider createProvider(MessagingType type) {
        return switch (type) {
            case REDIS -> new RedisMessengerProvider(this.plugin);
            default -> throw new IllegalArgumentException("Unknown messaging provider: " + type.name());
        };
    }
}
