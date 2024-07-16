package dev.xhyrom.lighteco.common.dependencies;

import com.google.common.collect.ImmutableList;

import dev.xhyrom.lighteco.common.dependencies.relocation.Relocation;

import lombok.Getter;

import java.util.List;

@Getter
public enum Dependency {
    /**
     * Somewhere we use brackets instad of dots, so we need to rewrite them
     * This is because gradle's shadow plugin relocates using replacing full paths (dots)
     */
    ASM("org.ow2.asm", "asm", "9.1"),
    ASM_COMMONS("org.ow2.asm", "asm-commons", "9.1"),
    JAR_RELOCATOR("me.lucko", "jar-relocator", "1.7"),
    HIKARI("com{}zaxxer", "HikariCP", "5.0.1", Relocation.of("hikari", "com{}zaxxer{}hikari")),
    H2_DRIVER("com.h2database", "h2", "2.1.214"),
    SQLITE_DRIVER("org.xerial", "sqlite-jdbc", "3.28.0"),
    MARIADB_DRIVER(
            "org{}mariadb{}jdbc",
            "mariadb-java-client",
            "3.1.3",
            Relocation.of("mariadb", "org{}mariadb{}jdbc")),
    MYSQL_DRIVER("mysql", "mysql-connector-java", "8.0.23", Relocation.of("mysql", "com{}mysql")),
    POSTGRESQL_DRIVER(
            "org{}postgresql",
            "postgresql",
            "42.6.0",
            Relocation.of("postgresql", "org{}postgresql")),
    JEDIS(
            "redis.clients",
            "jedis",
            "5.1.0",
            Relocation.of("jedis", "redis{}clients{}jedis"),
            Relocation.of("commonspool2", "org{}apache{}commons{}pool2")),
    SLF4J_SIMPLE("org.slf4j", "slf4j-simple", "1.7.30"),
    SLF4J_API("org.slf4j", "slf4j-api", "1.7.30"),
    COMMONS_POOL_2(
            "org.apache.commons",
            "commons-pool2",
            "2.9.0",
            Relocation.of("commonspool2", "org{}apache{}commons{}pool2"));

    private final String fullPath;
    private final String version;

    @Getter
    private final List<Relocation> relocations;

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    Dependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, new Relocation[0]);
    }

    Dependency(String groupId, String artifactId, String version, Relocation... relocations) {
        this.fullPath = String.format(
                MAVEN_FORMAT,
                rewriteEscape(groupId).replace('.', '/'),
                rewriteEscape(artifactId),
                version,
                rewriteEscape(artifactId),
                version);

        this.version = version;
        this.relocations = ImmutableList.copyOf(relocations);
    }

    private static String rewriteEscape(String path) {
        return path.replace("{}", ".");
    }

    public String getFileName() {
        return getFileName(null);
    }

    public String getFileName(String classifier) {
        String name = name().toLowerCase().replace('_', '-');
        String extra = classifier == null ? "" : "-" + classifier;

        return String.format("%s-%s.jar", name, this.version + extra);
    }
}
