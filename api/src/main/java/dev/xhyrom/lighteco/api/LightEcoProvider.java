package dev.xhyrom.lighteco.api;

import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import static org.jetbrains.annotations.ApiStatus.Internal;

@UtilityClass
public final class LightEcoProvider {
    private static LightEco instance;

    public static @NonNull LightEco get() {
        LightEco instance = LightEcoProvider.instance;
        if (instance == null) {
            throw new NotLoadedException();
        }

        return instance;
    }

    @Internal
    public static void set(LightEco instance) {
        if (LightEcoProvider.instance != null) {
            throw new IllegalStateException("LightEco is already loaded!");
        }

        LightEcoProvider.instance = instance;
    }

    private static final class NotLoadedException extends IllegalStateException {
        private NotLoadedException() {
            super("LightEco is not loaded yet!");
        }
    }
}
