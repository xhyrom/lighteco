package dev.xhyrom.lighteco.api.event;

import dev.xhyrom.lighteco.api.LightEco;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface LightEcoEvent {
    @NonNull LightEco getLightEco();

    @NonNull Class<? extends LightEcoEvent> getEventClass();
}
