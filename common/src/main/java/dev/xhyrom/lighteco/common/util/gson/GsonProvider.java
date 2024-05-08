package dev.xhyrom.lighteco.common.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonProvider {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static Gson get() {
        return GSON;
    }
}
