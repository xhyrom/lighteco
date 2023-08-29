package dev.xhyrom.lighteco.bukkit.commands;

import dev.jorel.commandapi.CommandAPICommand;

public interface Command {
    default CommandAPICommand build() {
        return null;
    }

    default CommandAPICommand[] multipleBuild() {
        return new CommandAPICommand[0];
    }
}
