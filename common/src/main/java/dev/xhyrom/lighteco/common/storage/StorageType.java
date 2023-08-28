package dev.xhyrom.lighteco.common.storage;

import java.util.List;
import java.util.stream.Stream;

public enum StorageType {
    MARIADB("MariaDB", "mariadb"),
    MYSQL("MySQL", "mysql"),

    SQLITE("SQLite", "sqlite"),
    H2("H2", "h2");

    private final String name;
    private final List<String> identifiers;

    StorageType(String name, String... identifiers) {
        this.name = name;
        this.identifiers = List.of(identifiers);
    }

    public static StorageType parse(String name) {
        return Stream.of(values())
                .filter(type -> type.identifiers.contains(name.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown storage type: " + name));
    }
}
