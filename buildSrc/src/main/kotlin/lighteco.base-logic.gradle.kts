plugins {
    `java-library`
    `maven-publish`
}

java {
    // toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml")) {
            expand(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "author" to "xHyroM"
            )
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

publishing {
    // Publishing to repo.jopga.me
    publications.create<MavenPublication>("mavenJava") {
        repositories.maven {
            url = uri("https://repo.jopga.me/releases")

            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }

        groupId = rootProject.group as String
        artifactId = project.name
        version = rootProject.version as String

        pom {
            name.set("LightEco")
        }

        artifact(tasks.named("jar"))
    }
}