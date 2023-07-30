plugins {
    id("lighteco.shadow-logic")
}

val Project.platform: String
    get() = project.name.split("-")[1]

tasks {
    shadowJar {
        archiveFileName.set("lighteco-${project.platform}-${project.version}.jar")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
}