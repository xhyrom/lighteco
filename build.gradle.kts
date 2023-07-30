import java.io.ByteArrayOutputStream

plugins {
    id("java")
}

val majorVersion = 0;
val minorVersion = 1;
val patchVersion = determinePatchVersion(project);

allprojects {
    group = "dev.xhyrom"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")

    ext {
        set("version", "$majorVersion.$minorVersion.$patchVersion")
    }

    repositories {
        mavenCentral()
    }
}

fun determinePatchVersion(project: Project): Int {
    val tagInfo = ByteArrayOutputStream()

    exec {
        commandLine("git", "describe", "--tags")
        standardOutput = tagInfo
    }

    val result = tagInfo.toString()

    return if (result.contains("-")) result.split("-")[1].toInt() else 0
}