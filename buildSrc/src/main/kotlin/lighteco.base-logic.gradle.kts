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
            url.set("https://github.com/xHyroM/lighteco")
            description.set(project.description)
            organization {
                name.set("xHyroM")
                url.set("https://xhyrom.dev")
            }
            developers {
                developer {
                    id.set("xHyroM")
                    name.set("xHyroM")
                    email.set("lol@xhyrom.dev")
                    timezone.set("Europe/Bratislava")
                    url.set("https://xhyrom.dev")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/xHyroM/lighteco.git")
                developerConnection.set("scm:git:git@github.com:xHyroM/lighteco.git")
                url.set("https://github.com/xHyroM/lighteco")
            }
            licenses {
                license {
                    name.set("Apache License 2.0")
                    url.set("https://github.com/xHyroM/lighteco/blob/main/LICENSE")
                    distribution.set("repo")
                }
            }
            ciManagement {
                system.set("GitHub Actions")
                url.set("https://github.com/xHyroM/lighteco/actions")
            }
        }

        artifact(tasks.named("jar")) {
            classifier = ""
        }

        artifact(tasks.named("sourcesJar"))
    }
}