import lol.koblizek.serverloader.plugin.server

plugins {
    id("lighteco.platform-logic")
    id("lol.koblizek.serverloader") version "1.2"
}

server {
    version = "1.20.1"
    type = "paper"
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