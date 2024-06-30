import lol.koblizek.serverloader.plugin.server

plugins {
    id("lighteco.platform-logic")
    id("lol.koblizek.serverloader") version "1.2"
}

server {
    version = "1.20.6"
    type = "purpur"

    // Minecraft 1.20.6 requires java 21
    java {
        targetCompatibility = JavaVersion.VERSION_21
        sourceCompatibility = JavaVersion.VERSION_21
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":lighteco-api"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}