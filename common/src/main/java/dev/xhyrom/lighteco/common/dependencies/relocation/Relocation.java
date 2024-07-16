package dev.xhyrom.lighteco.common.dependencies.relocation;

import lombok.Getter;

@Getter
public class Relocation {
    private static final String RELOCATION_PREFIX = "dev.xhyrom.lighteco.libraries.";

    public static Relocation of(String id, String pattern) {
        return new Relocation(pattern.replace("{}", "."), RELOCATION_PREFIX + id);
    }

    private final String pattern;
    private final String relocatedPattern;

    private Relocation(String pattern, String relocatedPattern) {
        this.pattern = pattern;
        this.relocatedPattern = relocatedPattern;
    }
}
