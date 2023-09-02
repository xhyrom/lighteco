rootProject.name = "lighteco-parent"

sequenceOf(
    "api",
    "common",
    "bukkit",
    "bukkittest",
    "sponge-9",
    "currency-money"
).forEach {
    include("lighteco-$it")
    project(":lighteco-$it").projectDir = file(it)
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.jopga.me/releases")
    }
}
