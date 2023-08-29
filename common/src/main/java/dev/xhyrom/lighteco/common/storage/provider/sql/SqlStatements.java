package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.common.storage.StorageType;

public enum SqlStatements {
    SAVE_USER_LOCAL_CURRENCY(
            "INSERT INTO {prefix}_{context}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON CONFLICT (uuid, currency_identifier) DO UPDATE SET balance=?3;",
            "INSERT INTO {prefix}_{context}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON DUPLICATE KEY UPDATE balance=?3;",
            "INSERT INTO {prefix}_{context}_users (uuid, currency_identifier, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE balance=?;"
    ),
    SAVE_USER_GLOBAL_CURRENCY(
            "INSERT INTO {prefix}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON CONFLICT (uuid, currency_identifier) DO UPDATE SET balance=?3;",
            "INSERT INTO {prefix}_users (uuid, currency_identifier, balance) VALUES (?1, ?2, ?3) ON DUPLICATE KEY UPDATE balance=?3;",
            "INSERT INTO {prefix}_users (uuid, currency_identifier, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE balance=?;"
    ),
    LOAD_WHOLE_USER(
            "SELECT currency_identifier, balance FROM ( SELECT currency_identifier, balance FROM '{prefix}_users' WHERE uuid = ?1 UNION ALL SELECT currency_identifier, balance FROM '{prefix}_{context}_users' WHERE uuid = ?1 ) AS combined_currencies;",
            "SELECT currency_identifier, balance FROM ( SELECT currency_identifier, balance FROM '{prefix}_users' WHERE uuid = ?1 UNION ALL SELECT currency_identifier, balance FROM '{prefix}_{context}_users' WHERE uuid = ?1 ) AS combined_currencies;",
            "SELECT currency_identifier, balance FROM ( SELECT currency_identifier, balance FROM '{prefix}_users' WHERE uuid = ? UNION ALL SELECT currency_identifier, balance FROM '{prefix}_{context}_users' WHERE uuid = ? ) AS combined_currencies;"
    );

    public final String sqlite;
    public final String mysql;
    public final String mariadb;

    SqlStatements(String sqlite, String mysql, String mariadb) {
        this.sqlite = sqlite;
        this.mysql = mysql;
        this.mariadb = mariadb;
    }

    public String get(StorageType implementationName) {
        switch (implementationName) {
            case SQLITE -> {
                return this.sqlite;
            }
            case H2, MYSQL -> {
                return this.mysql;
            }
            case MARIADB -> {
                return this.mariadb;
            }
        }

        throw new IllegalArgumentException("Unknown implementation: " + implementationName);
    }
}
