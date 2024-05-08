package dev.xhyrom.lighteco.common.messaging.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.xhyrom.lighteco.api.messenger.message.type.UserUpdateMessage;
import dev.xhyrom.lighteco.common.messaging.LightEcoMessagingService;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

public class UserUpdateMessageImpl extends AbstractMessage implements UserUpdateMessage {
    private static final MessageType TYPE = MessageType.USER_UPDATE;

    @Getter
    private final UUID userUniqueId;
    @Getter
    private final String currencyIdentifier;
    @Getter
    private final BigDecimal newBalance;

    public static UserUpdateMessage deserialize(UUID id, @NonNull JsonElement data) {
        JsonObject obj = data.getAsJsonObject();
        UUID userUniqueId = UUID.fromString(obj.get("u").getAsString());
        String currencyIdentifier = obj.get("c").getAsString();
        BigDecimal newBalance = obj.get("b").getAsBigDecimal();

        return new UserUpdateMessageImpl(id, userUniqueId, currencyIdentifier, newBalance);
    }

    public UserUpdateMessageImpl(UUID id, UUID userUniqueId, String currencyIdentifier, BigDecimal newBalance) {
        super(id);
        this.userUniqueId = userUniqueId;
        this.currencyIdentifier = currencyIdentifier;
        this.newBalance = newBalance;
    }

    @Override
    public @NonNull String serialize() {
        JsonObject data = new JsonObject();
        data.add("u", new JsonPrimitive(this.userUniqueId.toString()));
        data.add("c", new JsonPrimitive(this.currencyIdentifier));
        data.add("b", new JsonPrimitive(this.newBalance));

        return LightEcoMessagingService.serialize(TYPE, getId(), data);
    }
}
