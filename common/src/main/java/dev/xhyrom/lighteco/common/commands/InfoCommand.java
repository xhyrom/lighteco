package dev.xhyrom.lighteco.common.commands;

import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("lighteco", "LightEco information");
    }

    @Override
    public CommandNode<CommandSource> build() {
        return builder()
                .requires(source -> source.sender().eligible("lighteco.command.info"))
                .executes(context -> {
                    LightEcoPlugin plugin = context.getSource().plugin();
                    CommandSender sender = context.getSource().sender();

                    sender.sendMessage(MiniMessage.miniMessage().deserialize(
                            "<#fa5246><bold>LightEco</bold></#fa5246> <dark_gray>(<#d6766f>v<version><dark_gray>) <white>on <#d6766f><platform>",
                            Placeholder.parsed("version", plugin.getBootstrap().getVersion()),
                            Placeholder.parsed("platform", plugin.getPlatformType().getName())
                    ));

                    return SINGLE_SUCCESS;
                })
                .build();
    }
}
