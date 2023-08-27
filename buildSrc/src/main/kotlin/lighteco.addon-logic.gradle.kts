plugins {
    id("lighteco.shadow-logic")
}

val Project.addon: String
    get() = project.name.split("-").drop(1).joinToString("-")

tasks {
    shadowJar {
        archiveFileName.set("lighteco-${project.addon}-${project.version}.jar")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs/addons"))
    }
}