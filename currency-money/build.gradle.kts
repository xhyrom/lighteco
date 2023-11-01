plugins {
    id("lighteco.addon-logic")
}

description = "Addon that adds main currency to LightEco that uses Vault to hook into other economy plugins."

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
    compileOnly(project(":lighteco-api"))

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    // This can be compileOnly because it's included in common (lighteco-platform)
    // Reduce duplication and size
    compileOnly("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.0-beta.5")
    compileOnly("eu.okaeri:okaeri-configs-validator-okaeri:5.0.0-beta.5")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.checkerframework:checker-qual:3.8.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks.shadowJar {
    relocate("eu.okaeri.configs", "dev.xhyrom.lighteco.libraries.okaeri.configs")
    relocate("eu.okaeri.validator", "dev.xhyrom.lighteco.libraries.okaeri.validator")
}