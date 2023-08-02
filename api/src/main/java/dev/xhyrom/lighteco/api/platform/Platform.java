package dev.xhyrom.lighteco.api.platform;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface Platform {
    @NonNull Type getType();

    enum Type {
        BUKKIT("Bukkit"),
        VELOCITY("Velocity"),
        BUNGEECORD("BungeeCord");

        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        public String getName() {
            return displayName;
        }

        public boolean isLocal() {
            return this == BUKKIT;
        }

        public boolean isProxy() {
            return this == VELOCITY || this == BUNGEECORD;
        }
    }
}
