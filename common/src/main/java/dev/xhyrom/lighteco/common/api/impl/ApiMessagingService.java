package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.messaging.MessagingService;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.common.messaging.InternalMessagingService;

public class ApiMessagingService implements MessagingService {
    private final InternalMessagingService handle;

    public ApiMessagingService(InternalMessagingService handle) {
        this.handle = handle;
    }

    @Override
    public void pushUserUpdate(User user, Currency currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency internalCurrency = this.handle.getPlugin()
                .getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handle.pushUserUpdate(ApiUser.cast(user), internalCurrency);
    }
}
