rootProject.name = "lighteco-parent"

sequenceOf(
    "api",
    "common",
    "paper",
    "sponge-9",
    "currency-money",
    "test"
).forEach {
    include("lighteco-$it")
    project(":lighteco-$it").projectDir = file(it)
}

sequenceOf(
    "paper"
).forEach {
    include("lighteco-test-$it")
    project(":lighteco-test-$it").projectDir = file("test/$it")
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.jopga.me/releases")
    }
}
