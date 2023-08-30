package dev.xhyrom.lighteco.currency.money.common.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class Config extends OkaeriConfig {
    @Comment("Currency name")

    @Comment("In singular form")
    public String currencyNameSingular = "Dollar";
    @Comment("In plural form")
    public String currencyNamePlural = "Dollars";

    @Comment("Currency code (ISO 4217 code of currency)")
    @Comment("Used for formatting currency")
    @Comment("See https://en.wikipedia.org/wiki/ISO_4217 for more information")
    public String currencyCode = "USD";
}
