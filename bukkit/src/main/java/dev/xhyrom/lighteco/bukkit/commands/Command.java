package dev.xhyrom.lighteco.bukkit.commands;

import dev.jorel.commandapi.CommandAPICommand;

import java.util.List;

public interface Command {
    default CommandAPICommand build() {
        return null;
    };
    default CommandAPICommand[] multipleBuild() {
        return null;
    };
}
