package dev.xhyrom.lighteco.common.messaging;

import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

public interface InternalMessagingService {
    LightEcoPlugin getPlugin();

    void pushUserUpdate(User user, Currency currency);

    void shutdown();
}
