rootProject.name = "lighteco-parent"

sequenceOf(
    "api",
    "common",
    "paper",
    "bukkittest",
    "sponge-8",
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
