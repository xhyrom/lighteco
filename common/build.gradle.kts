plugins {
    id("lighteco.shadow-logic")
}

dependencies {
    api(project(":lighteco-api"))
    api("org.checkerframework:checker-qual:3.8.0")
    api("net.kyori:adventure-api:4.12.0") {
        exclude(module = "adventure-bom")
        exclude(module = "checker-qual")
        exclude(module = "annotations")
    }
    api("net.kyori:adventure-text-minimessage:4.14.0") {
        exclude(module = "adventure-bom")
        exclude(module = "adventure-api")
    }
    api("com.google.guava:guava:32.1.2-jre")

    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.0-beta.5")
    implementation("eu.okaeri:okaeri-configs-validator-okaeri:5.0.0-beta.5")

    compileOnly("com.zaxxer:HikariCP:5.0.1")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.jetbrains:annotations:20.1.0")
}