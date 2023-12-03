package dev.xhyrom.lighteco.common.messaging;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.xhyrom.lighteco.api.messenger.IncomingMessageConsumer;
import dev.xhyrom.lighteco.api.messenger.Messenger;
import dev.xhyrom.lighteco.api.messenger.MessengerProvider;
import dev.xhyrom.lighteco.api.messenger.message.Message;
import dev.xhyrom.lighteco.api.messenger.message.type.UserUpdateMessage;
import dev.xhyrom.lighteco.common.cache.ExpiringSet;
import dev.xhyrom.lighteco.common.messaging.message.MessageType;
import dev.xhyrom.lighteco.common.messaging.message.UserUpdateMessageImpl;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import dev.xhyrom.lighteco.common.util.gson.GsonProvider;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LightEcoMessagingService implements InternalMessagingService, IncomingMessageConsumer {
    @Getter
    private final LightEcoPlugin plugin;
    private final ExpiringSet<UUID> receivedMessages;

    private final Messenger messenger;
    private final MessengerProvider provider;

    public LightEcoMessagingService(LightEcoPlugin plugin, MessengerProvider provider) {
        this.plugin = plugin;

        this.provider = provider;
        this.messenger = provider.obtain(this);
        this.receivedMessages = new ExpiringSet<>(30, TimeUnit.MINUTES);
    }

    private UUID generateMessageId() {
        UUID id = UUID.randomUUID();

        this.receivedMessages.add(id);
        return id;
    }

    @Override
    public void pushUserUpdate(User user, Currency currency) {
        this.plugin.getBootstrap().getScheduler().async().execute(() ->
                this.messenger.sendOutgoingMessage(
                        new UserUpdateMessageImpl(generateMessageId(), user.getUniqueId(), currency.getIdentifier(), user.getBalance(currency)),
                        currency.getType() == dev.xhyrom.lighteco.api.model.currency.Currency.Type.GLOBAL
                )
        );
    }

    public static @NonNull String serialize(MessageType type, UUID id, JsonElement content) {
        JsonObject data = new JsonObject();

        data.add("i", new JsonPrimitive(id.toString()));
        data.add("t", new JsonPrimitive(type.name()));
        data.add("c", content);

        return GsonProvider.get().toJson(data);
    }

    @Override
    public void consumeIncomingMessage(@NonNull Message message) {
        if (!this.receivedMessages.add(message.getId())) {
            return;
        }

        this.processIncomingMessage(message);
    }

    @Override
    public void consumeRawIncomingMessage(@NonNull String message) {
        try {
            deserializeAndConsumeRawIncomingMessage(message);
        } catch (Exception e) {
            this.plugin.getBootstrap().getLogger().warn("Failed to deserialize incoming message: " + message, e);
        }
    }

    private void deserializeAndConsumeRawIncomingMessage(@NonNull String message) {
        JsonObject parsed = GsonProvider.get().fromJson(message, JsonObject.class);

        JsonElement idElement = parsed.get("i");
        if (idElement == null) {
            throw new IllegalStateException("Missing message id: " + message);
        }

        UUID id = UUID.fromString(idElement.getAsString());

        if (!this.receivedMessages.add(id)) {
            return;
        }

        JsonElement typeElement = parsed.get("t");
        if (typeElement == null) {
            throw new IllegalStateException("Missing message type: " + message);
        }

        MessageType type = MessageType.valueOf(typeElement.getAsString());

        @Nullable JsonElement contentElement = parsed.get("c");

        Message deserialized;
        switch (type) {
            case USER_UPDATE:
                deserialized = UserUpdateMessageImpl.deserialize(id, contentElement);
                break;
            default:
                return;
        }

        this.processIncomingMessage(deserialized);
    }

    private void processIncomingMessage(Message message) {
        if (message instanceof UserUpdateMessage userUpdateMessage) {
            this.plugin.getBootstrap().getScheduler().async().execute(() -> {
                User user = this.plugin.getUserManager().getIfLoaded(userUpdateMessage.getUserUniqueId());
                if (user == null) {
                    return;
                }

                Currency currency = this.plugin.getCurrencyManager().getIfLoaded(userUpdateMessage.getCurrencyIdentifier());
                if (currency == null) {
                    return;
                }

                user.setBalance(currency, userUpdateMessage.getNewBalance(), false, false);
            });
        } else {
            throw new IllegalStateException("Unknown message type: " + message.getClass().getName());
        }
    }

    @Override
    public void shutdown() {
        this.messenger.close();
    }
}
