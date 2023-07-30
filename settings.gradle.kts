rootProject.name = "lighteco-parent"

sequenceOf(
    "api",
    "common",
    "bukkit",
    "velocity"
).forEach {
    include("lighteco-$it")
    project(":lighteco-$it").projectDir = file(it)
}
