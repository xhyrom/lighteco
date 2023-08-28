plugins {
    id("lighteco.platform-logic")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
}

dependencies {
    implementation(project(":lighteco-common"))

    implementation("dev.jorel:commandapi-bukkit-shade:9.1.0")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.3")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.checkerframework:checker-qual:3.8.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "dev.xhyrom.lighteco.libraries.commandapi")

    // common
    relocate("eu.okaeri.configs", "dev.xhyrom.lighteco.libraries.okaeri.configs")
    relocate("eu.okaeri.validator", "dev.xhyrom.lighteco.libraries.okaeri.validator")

    relocate("net.kyori.adventure", "dev.xhyrom.lighteco.libraries.net.kyori.adventure")
    relocate("net.kyori.examination", "dev.xhyrom.lighteco.libraries.net.kyori.examination")

    relocate("org.yaml.snakeyaml", "dev.xhyrom.lighteco.libraries.org.yaml.snakeyaml")

    relocate("com.zaxxer.hikari", "dev.xhyrom.lighteco.libraries.com.zaxxer.hikari")
}