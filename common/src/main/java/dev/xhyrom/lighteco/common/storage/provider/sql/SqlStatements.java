package dev.xhyrom.lighteco.common.storage.provider.sql;

public enum SqlStatements {
    SAVE_USER_LOCAL_CURRENCY(
            "INSERT INTO {prefix}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON CONFLICT (uuid, currency_identifier) DO UPDATE SET balance=?3;",
            "INSERT INTO {prefix}_{context}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON DUPLICATE KEY UPDATE balance=?3;"
    ),
    SAVE_USER_GLOBAL_CURRENCY(
        "INSERT INTO {prefix}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON CONFLICT (uuid, currency_identifier) DO UPDATE SET balance=?3;",
        "INSERT INTO {prefix}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON DUPLICATE KEY UPDATE balance=?3;"
    );

    public final String sqlite;
    public final String mysql;

    SqlStatements(String sqlite, String mysql) {
        this.sqlite = sqlite;
        this.mysql = mysql;
    }

    public String get(SqlImplementation implementationName) {
        switch (implementationName) {
            case SQLITE -> {
                return this.sqlite;
            }
            case MYSQL -> {
                return this.mysql;
            }
        }

        throw new IllegalArgumentException("Unknown implementation: " + implementationName);
    }
}
