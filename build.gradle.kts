import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("org.sonarqube") version "4.2.1.3168"
    id("com.diffplug.spotless") version "6.25.0"
}

val majorVersion = 0
val minorVersion = 1
val patchVersion = determinePatchVersion(project)
val commitHash = determineCommitHash(project)

defaultTasks("spotlessApply")

repositories {
    mavenCentral()
}

allprojects {
    group = "dev.xhyrom"
    version = "$majorVersion.$minorVersion.$patchVersion"
    description = "Incredibly fast, lightweight, and modular plugin that excels across multiple platforms."

    ext {
        set("version", "$majorVersion.$minorVersion.$patchVersion+$commitHash")
        set("description", description)
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
        maven("https://storehouse.okaeri.eu/repository/maven-public/")
        maven("https://libraries.minecraft.net")
    }
}

spotless {
    java {
        importOrder()
        removeUnusedImports()

        palantirJavaFormat().style("AOSP")
        formatAnnotations()

        target("api/src/main/java/**", "common/src/main/java/**", "currency-money/src/main/java/**", "paper/src/main/java/**", "sponge-8/src/main/java/**", "test/**/src/main/java/**")
    }
}

fun determinePatchVersion(project: Project): Int {
    val tagInfo = ByteArrayOutputStream()

    return try {
        exec {
            commandLine("git", "describe", "--tags")
            standardOutput = tagInfo
        }

        val result = tagInfo.toString()

        if (result.contains("-")) result.split("-")[1].toInt() else 0
    } catch (e: Exception) {
        0
    }
}

fun determineCommitHash(project: Project): String {
    val commitHashInfo = ByteArrayOutputStream()

    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = commitHashInfo
    }

    return commitHashInfo.toString().strip()
}