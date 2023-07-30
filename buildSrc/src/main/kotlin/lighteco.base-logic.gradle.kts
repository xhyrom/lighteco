plugins {
    `java-library`
    `maven-publish`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml")) {
            expand(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "author" to "xHyroM"
            )
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}