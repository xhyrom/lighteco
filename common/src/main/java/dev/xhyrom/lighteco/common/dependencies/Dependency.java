package dev.xhyrom.lighteco.common.dependencies;

import lombok.Getter;

public enum Dependency {
    H2_DRIVER(
        "com.h2database",
        "h2",
        "2.1.214"
    ),
    SQLITE_DRIVER(
        "org.xerial",
        "sqlite-jdbc",
        "3.28.0"
    ),
    MARIADB_DRIVER(
        "org.mariadb.jdbc",
        "mariadb-java-client",
        "3.1.3"
    ),
    MYSQL_DRIVER(
        "mysql",
        "mysql-connector-java",
        "8.0.23"
    ),
    POSTGRESQL_DRIVER(
        "org.postgresql",
        "postgresql",
        "42.6.0"
    );

    @Getter
    private final String fullPath;
    private final String groupId;
    private final String artifactId;
    private final String version;

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    Dependency(String groupId, String artifactId, String version) {
        this.fullPath = String.format(MAVEN_FORMAT,
                groupId.replace('.', '/'),
                artifactId,
                version,
                artifactId,
                version
        );

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getFileName() {
        String name = name().toLowerCase().replace('_', '-');

        return String.format("%s-%s.jar", name, this.version);
    }
}
