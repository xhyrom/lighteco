package dev.xhyrom.lighteco.common.messaging;

import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;

public interface MessagingService extends AutoCloseable {
    void pushUserUpdate(User user, Currency currency);
}
