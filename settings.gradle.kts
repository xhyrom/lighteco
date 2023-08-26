rootProject.name = "lighteco-parent"

sequenceOf(
    "api",
    "common",
    "bukkit",
    "bukkittest",
    "velocity"
).forEach {
    include("lighteco-$it")
    project(":lighteco-$it").projectDir = file(it)
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}