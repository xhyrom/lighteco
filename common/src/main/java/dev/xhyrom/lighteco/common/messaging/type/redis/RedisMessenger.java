package dev.xhyrom.lighteco.common.messaging.type.redis;

import dev.xhyrom.lighteco.api.messenger.IncomingMessageConsumer;
import dev.xhyrom.lighteco.api.messenger.Messenger;
import dev.xhyrom.lighteco.api.messenger.message.OutgoingMessage;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import redis.clients.jedis.*;

public class RedisMessenger implements Messenger {
    private static final String CHANNEL = "lighteco:{}:messages";

    @Getter
    private final String[] channels;

    private final LightEcoPlugin plugin;
    private final IncomingMessageConsumer consumer;

    private UnifiedJedis jedis;
    private Subscription sub;

    public RedisMessenger(LightEcoPlugin plugin, IncomingMessageConsumer consumer) {
        this.plugin = plugin;
        this.consumer = consumer;

        this.channels = new String[] {
            CHANNEL.replace("{}:", ""), CHANNEL.replace("{}", this.plugin.getConfig().server)
        };
    }

    public void init(
            @Nullable String address, @Nullable String username, String password, boolean ssl) {
        this.init(new JedisPooled(parseAddress(address), jedisConfig(username, password, ssl)));
    }

    private void init(UnifiedJedis jedis) {
        this.jedis = jedis;
        this.sub = new Subscription(this);

        this.plugin.getBootstrap().getScheduler().async().execute(() -> {
            this.jedis.subscribe(this.sub, this.getChannels());
        });
    }

    private static JedisClientConfig jedisConfig(
            @Nullable String username, @Nullable String password, boolean ssl) {
        return DefaultJedisClientConfig.builder()
                .user(username)
                .password(password)
                .ssl(ssl)
                .timeoutMillis(Protocol.DEFAULT_TIMEOUT)
                .build();
    }

    private static HostAndPort parseAddress(String address) {
        String[] addressSplit = address.split(":");
        String host = addressSplit[0];
        int port =
                addressSplit.length > 1 ? Integer.parseInt(addressSplit[1]) : Protocol.DEFAULT_PORT;

        return new HostAndPort(host, port);
    }

    @Override
    public void sendOutgoingMessage(@NonNull OutgoingMessage message, boolean global) {
        this.jedis.publish(global ? getChannels()[0] : getChannels()[1], message.serialize());
    }

    @Override
    public void close() {
        this.sub.unsubscribe();
        this.jedis.close();
    }

    private static class Subscription extends JedisPubSub {
        private final RedisMessenger messenger;

        public Subscription(RedisMessenger messenger) {
            this.messenger = messenger;
        }

        @Override
        public void onMessage(String channel, String message) {
            String[] channels = this.messenger.getChannels();
            if (!channel.equals(channels[0]) && !channel.equals(channels[1])) {
                return;
            }

            this.messenger.consumer.consumeRawIncomingMessage(message);
        }
    }
}
