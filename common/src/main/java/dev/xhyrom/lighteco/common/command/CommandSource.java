package dev.xhyrom.lighteco.common.command;

import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import org.checkerframework.checker.nullness.qual.NonNull;

public record CommandSource(@NonNull LightEcoPlugin plugin, @NonNull CommandSender sender) {}
