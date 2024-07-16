package dev.xhyrom.lighteco.common.storage.provider.sql;

import dev.xhyrom.lighteco.common.storage.StorageType;

public enum SqlStatements {
    SAVE_USER_LOCAL_CURRENCY(
            "INSERT INTO '{prefix}_local_{context}_{currency}_users' (uuid, balance) VALUES (?1, ?2) ON CONFLICT (uuid) DO UPDATE SET balance=?2;",
            "INSERT INTO '{prefix}_local_{context}_{currency}_users' (uuid, balance) VALUES (?1, ?2) ON DUPLICATE KEY UPDATE balance=?2;",
            "INSERT INTO '{prefix}_local_{context}_{currency}_users' (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance=?;",
            "INSERT INTO '{prefix}_local_{context}_{currency}_users' (uuid, balance) VALUES (?1, ?2) ON CONFLICT (uuid) DO UPDATE SET balance=?2;"),
    SAVE_USER_GLOBAL_CURRENCY(
            "INSERT INTO '{prefix}_global_{currency}_users' (uuid, balance) VALUES (?1, ?2) ON CONFLICT (uuid) DO UPDATE SET balance=?2;",
            "INSERT INTO '{prefix}_global_{currency}_users' (uuid, balance) VALUES (?1, ?2) ON DUPLICATE KEY UPDATE balance=?2;",
            "INSERT INTO '{prefix}_global_{currency}_users' (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance=?;",
            "INSERT INTO '{prefix}_global_{currency}_users' (uuid, balance) VALUES (?1, ?2) ON CONFLICT (uuid) DO UPDATE SET balance=?2;"),
    LOAD_LOCAL_CURRENCY_USER(
            "SELECT {identifier} AS name, balance FROM '{prefix}_local_{context}_{currency}_users' WHERE uuid = ?1",
            "SELECT {identifier} AS name, balance FROM '{prefix}_local_{context}_{currency}_users' WHERE uuid = ?1",
            "SELECT {identifier} AS name, balance FROM '{prefix}_local_{context}_{currency}_users' WHERE uuid = ?",
            "SELECT {identifier} AS name, balance FROM '{prefix}_local_{context}_{currency}_users' WHERE uuid = ?1"),
    LOAD_GLOBAL_CURRENCY_USER(
            "SELECT {identifier} AS name, balance FROM '{prefix}_global_{currency}_users' WHERE uuid = ?1",
            "SELECT {identifier} AS name, balance FROM '{prefix}_global_{currency}_users' WHERE uuid = ?1",
            "SELECT {identifier} AS name, balance FROM '{prefix}_global_{currency}_users' WHERE uuid = ?",
            "SELECT {identifier} AS name, balance FROM '{prefix}_global_{currency}_users' WHERE uuid = ?1");

    public final String sqlite;
    public final String mysql;
    public final String mariadb;
    public final String postgresql;

    SqlStatements(String sqlite, String mysql, String mariadb, String postgresql) {
        this.sqlite = sqlite;
        this.mysql = mysql;
        this.mariadb = mariadb;
        this.postgresql = postgresql != null ? postgresql : mariadb;
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
            case POSTGRESQL -> {
                return this.postgresql;
            }
            default -> throw new IllegalArgumentException(
                    "Unknown implementation: " + implementationName);
        }
    }

    public static boolean mustDuplicateParameters(StorageType implementationName) {
        return implementationName == StorageType.MARIADB
                || implementationName == StorageType.POSTGRESQL;
    }
}
