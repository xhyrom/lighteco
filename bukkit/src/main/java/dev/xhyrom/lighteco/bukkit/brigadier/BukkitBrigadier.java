package dev.xhyrom.lighteco.bukkit.brigadier;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.lang.reflect.Field;
import java.util.Map;

public class BukkitBrigadier {
    private static final Field CUSTOM_SUGGESTIONS_FIELD;

    protected static final Field COMMAND_EXECUTE_FUNCTION_FIELD;

    private static final Field CHILDREN_FIELD;
    private static final Field LITERALS_FIELD;
    private static final Field ARGUMENTS_FIELD;
    private static final Field[] CHILDREN_FIELDS;

    protected static final SuggestionProvider<?> DUMMY_SUGGESTION_PROVIDER;

    static {
        try {
            CUSTOM_SUGGESTIONS_FIELD = ArgumentCommandNode.class.getDeclaredField("customSuggestions");
            CUSTOM_SUGGESTIONS_FIELD.setAccessible(true);

            COMMAND_EXECUTE_FUNCTION_FIELD = CommandNode.class.getDeclaredField("command");
            COMMAND_EXECUTE_FUNCTION_FIELD.setAccessible(true);

            CHILDREN_FIELD = CommandNode.class.getDeclaredField("children");
            LITERALS_FIELD = CommandNode.class.getDeclaredField("literals");
            ARGUMENTS_FIELD = CommandNode.class.getDeclaredField("arguments");
            CHILDREN_FIELDS = new Field[]{CHILDREN_FIELD, LITERALS_FIELD, ARGUMENTS_FIELD};

            for (Field field : CHILDREN_FIELDS) {
                field.setAccessible(true);
            }

            DUMMY_SUGGESTION_PROVIDER = (context, builder) -> { throw new UnsupportedOperationException(); };
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void setCustomSuggestionProvider(CommandNode<?> node, SuggestionProvider<?> suggestionProvider) {
        if (suggestionProvider != null && node instanceof ArgumentCommandNode) {
            ArgumentCommandNode<?, ?> argumentNode = (ArgumentCommandNode<?, ?>) node;

            try {
                CUSTOM_SUGGESTIONS_FIELD.set(argumentNode, suggestionProvider);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (CommandNode<?> child : node.getChildren()) {
            setCustomSuggestionProvider(child, suggestionProvider);
        }
    }

    public static void removeChild(RootCommandNode root, String name) {
        try {
            for (Field field : CHILDREN_FIELDS) {
                Map<String, ?> children = (Map<String, ?>) field.get(root);
                children.remove(name);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
