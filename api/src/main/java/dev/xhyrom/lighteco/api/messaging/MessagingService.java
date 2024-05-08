package dev.xhyrom.lighteco.api.messaging;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;

public interface MessagingService {
    void pushUserUpdate(User user, Currency currency);
}
