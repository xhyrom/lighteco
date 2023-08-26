package dev.xhyrom.lighteco.api.event.currency;

import dev.xhyrom.lighteco.api.event.LightEcoEvent;
import dev.xhyrom.lighteco.api.event.util.Position;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CurrencyRegisterEvent extends LightEcoEvent {
    @Position(0)
    @NonNull Currency getCurrency();
}
