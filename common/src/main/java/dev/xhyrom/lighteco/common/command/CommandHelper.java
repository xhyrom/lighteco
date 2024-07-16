package dev.xhyrom.lighteco.common.command;

import com.mojang.brigadier.context.CommandContext;

import dev.xhyrom.lighteco.common.command.argument.type.OfflineUserArgument;
import dev.xhyrom.lighteco.common.command.exception.LockedUserException;
import dev.xhyrom.lighteco.common.config.message.CurrencyMessageConfig;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import lombok.experimental.UtilityClass;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.Map;

@UtilityClass
public class CommandHelper {
    public static User getUser(CommandContext<CommandSource> context) {
        LightEcoPlugin plugin = context.getSource().plugin();
        CommandSender sender = context.getSource().sender();

        User target = null;
        try {
            target = OfflineUserArgument.getOfflineUser(context, "target");
        } catch (LockedUserException e) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(plugin.getConfig().messages.wait));
        }

        if (target == null || target.getUsername() == null) {
            String userName = context.getArgument("target", String.class);

            sender.sendMessage(MiniMessage.miniMessage()
                    .deserialize(
                            plugin.getConfig().messages.userNotFound,
                            Placeholder.parsed("username", userName)));
            return null;
        }

        return target;
    }

    public static CurrencyMessageConfig getCurrencyMessageConfig(
            LightEcoPlugin plugin, Currency currency) {
        Map<String, CurrencyMessageConfig> config = plugin.getConfig().messages.currency;
        CurrencyMessageConfig currencyMessageConfig = config.get(currency.getIdentifier());

        if (currencyMessageConfig == null) {
            return config.get("default");
        }

        return currencyMessageConfig;
    }
}
