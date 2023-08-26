plugins {
    id("lighteco.shadow-logic")
}

dependencies {
    api(project(":lighteco-api"))

    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.0-beta.5")
    implementation("eu.okaeri:okaeri-configs-validator-okaeri:5.0.0-beta.5")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    compileOnly("org.checkerframework:checker-qual:3.8.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
}