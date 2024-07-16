package dev.xhyrom.lighteco.common.messaging.type.redis;

import dev.xhyrom.lighteco.api.messenger.IncomingMessageConsumer;
import dev.xhyrom.lighteco.api.messenger.Messenger;
import dev.xhyrom.lighteco.api.messenger.MessengerProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RedisMessengerProvider implements MessengerProvider {
    private final LightEcoPlugin plugin;

    public RedisMessengerProvider(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NonNull Messenger obtain(@NonNull IncomingMessageConsumer consumer) {
        RedisMessenger messenger = new RedisMessenger(this.plugin, consumer);

        String address = this.plugin.getConfig().messaging.data.address;
        String username = this.plugin.getConfig().messaging.data.username;
        String password = this.plugin.getConfig().messaging.data.password;

        if (password.isEmpty()) password = null;
        if (username.isEmpty()) username = null;

        boolean ssl = this.plugin.getConfig().messaging.data.ssl;

        messenger.init(address, username, password, ssl);

        return messenger;
    }
}
