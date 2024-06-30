plugins {
    id("lighteco.platform-logic")
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    implementation(project(":lighteco-common"))

    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("me.lucko:commodore:2.2")

    compileOnly("io.papermc.paper:paper-api:1.19.1-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-mojangapi:1.19.1-R0.1-SNAPSHOT")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.3")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.checkerframework:checker-qual:3.8.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks.shadowJar {
    dependencies {
        exclude(dependency("com.mojang:brigadier"))
    }
    relocate("me.lucko.commodore", "dev.xhyrom.lighteco.libraries.commodore")

    // common
    relocate("eu.okaeri.configs", "dev.xhyrom.lighteco.libraries.okaeri.configs")
    relocate("eu.okaeri.validator", "dev.xhyrom.lighteco.libraries.okaeri.validator")

    relocate("org.yaml.snakeyaml", "dev.xhyrom.lighteco.libraries.org.yaml.snakeyaml")

    relocate("org.mariadb.jdbc", "dev.xhyrom.lighteco.libraries.mariadb")
    relocate("com.mysql", "dev.xhyrom.lighteco.libraries.mysql")
    relocate("org.postgresql", "dev.xhyrom.lighteco.libraries.postgresql")
    relocate("com.zaxxer.hikari", "dev.xhyrom.lighteco.libraries.hikari")
}