plugins {
    id("lighteco.platform-logic")
}

dependencies {
    implementation(project(":lighteco-common"))

    compileOnly("org.spongepowered:spongeapi:9.0.0")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.checkerframework:checker-qual:3.8.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks.shadowJar {
    // common
    relocate("eu.okaeri.configs", "dev.xhyrom.lighteco.libraries.okaeri.configs")
    relocate("eu.okaeri.validator", "dev.xhyrom.lighteco.libraries.okaeri.validator")

    relocate("org.yaml.snakeyaml", "dev.xhyrom.lighteco.libraries.org.yaml.snakeyaml")

    relocate("org.mariadb.jdbc", "dev.xhyrom.lighteco.libraries.mariadb")
    relocate("com.mysql", "dev.xhyrom.lighteco.libraries.mysql")
    relocate("org.postgresql", "dev.xhyrom.lighteco.libraries.postgresql")
    relocate("com.zaxxer.hikari", "dev.xhyrom.lighteco.libraries.hikari")
}