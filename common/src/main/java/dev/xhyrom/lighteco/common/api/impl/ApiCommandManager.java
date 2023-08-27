package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.manager.CommandManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ApiCommandManager extends ApiAbstractManager<dev.xhyrom.lighteco.common.manager.command.CommandManager> implements CommandManager {
    public ApiCommandManager(LightEcoPlugin plugin, dev.xhyrom.lighteco.common.manager.command.CommandManager handler) {
        super(plugin, handler);
    }

    @Override
    public void registerCurrencyCommand(@NonNull Currency currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.plugin.getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handler.registerCurrencyCommand(internal);
    }

    @Override
    public void registerCurrencyCommand(@NonNull Currency currency, boolean main) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = this.plugin.getCurrencyManager()
                .getIfLoaded(currency.getIdentifier());

        this.handler.registerCurrencyCommand(internal, main);
    }
}