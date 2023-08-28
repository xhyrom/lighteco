CREATE TABLE IF NOT EXISTS `{prefix}_users` (
    `uuid`                  VARCHAR(36)     NOT NULL,
    `currency_identifier`   VARCHAR(255)    NOT NULL,
    `balance`               DECIMAL(10, 2)  NOT NULL,
    PRIMARY KEY (`uuid`, `currency_identifier`)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `{prefix}_{context}_users` (
    `uuid`                  VARCHAR(36)     NOT NULL,
    `currency_identifier`   VARCHAR(255)    NOT NULL,
    `balance`               DECIMAL(10, 2)  NOT NULL,
    PRIMARY KEY (`uuid`, `currency_identifier`)
) DEFAULT CHARSET = utf8mb4;