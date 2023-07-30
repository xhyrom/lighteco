plugins {
    id("lighteco.base-logic")
    id("com.github.johnrengelman.shadow")
}

tasks {
    named<Jar>("jar") {
        archiveClassifier.set("unshaded")
    }
    named("build") {
        dependsOn(tasks.shadowJar)
    }
}