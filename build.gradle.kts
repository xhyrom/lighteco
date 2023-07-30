import java.io.ByteArrayOutputStream

plugins {
    id("java")
}

val majorVersion = 0
val minorVersion = 1
val patchVersion = determinePatchVersion(project)
val commitHash = determineCommitHash(project)

allprojects {
    group = "dev.xhyrom"
    version = "$majorVersion.$minorVersion.$patchVersion"

    ext {
        set("version", "$majorVersion.$minorVersion.$patchVersion+$commitHash")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
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

    return commitHashInfo.toString()
}