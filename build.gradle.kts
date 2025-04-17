import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.14"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"
val jitpackMavenUrl = "https://jitpack.io"

paperweight {
    upstreams.register("divinemc") {
        repo = github("BX-Team", "DivineMC")
        ref = providers.gradleProperty("divineRef")

        patchFile {
            path = "divinemc-server/build.gradle.kts"
            outputFile = file("basiclandmc-server/build.gradle.kts")
            patchFile = file("basiclandmc-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "divinemc-api/build.gradle.kts"
            outputFile = file("basiclandmc-api/build.gradle.kts")
            patchFile = file("basiclandmc-api/build.gradle.kts.patch")
        }
        patchRepo("paperApi") {
            upstreamPath = "paper-api"
            patchesDir = file("basiclandmc-api/paper-patches")
            outputDir = file("paper-api")
        }
        patchRepo("purpurApi") {
            upstreamPath = "purpur-api"
            patchesDir = file("basiclandmc-api/purpur-patches")
            outputDir = file("purpur-api")
        }
        patchDir("divinemcApi") {
            upstreamPath = "divinemc-api"
            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches")
            patchesDir = file("basiclandmc-api/divinemc-patches")
            outputDir = file("divinemc-api")
        }
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.compileJava {
        options.compilerArgs.add("-Xlint:-deprecation")
        options.isWarnings = false
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        options.isFork = true
        options.forkOptions.memoryMaximumSize = "4G"
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
        maven(jitpackMavenUrl)
        maven(paperMavenPublicUrl)
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven("http://nexus.basicland.cz:8081/repository/dev-snapshots/") {
                name = "BasicLandMCSnapshots"
                isAllowInsecureProtocol = true
                credentials(PasswordCredentials::class)
            }
        }
    }
}
