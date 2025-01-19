import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.14"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

//paperweight {
//    serverProject.set(project(":basiclandmc-server"))
//
//    remapRepo.set("https://maven.fabricmc.net/")
//    decompileRepo.set(paperMavenPublicUrl)
//
//    useStandardUpstream("divinemc") {
//        url.set(github("DivineMC", "DivineMC"))
//        ref.set(providers.gradleProperty("divineRef"))
//
//        withStandardPatcher {
//            apiSourceDirPath.set("DivineMC-API")
//            serverSourceDirPath.set("DivineMC-Server")
//
//            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
//            apiOutputDir.set(layout.projectDirectory.dir("BasicLandMC-API"))
//
//            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
//            serverOutputDir.set(layout.projectDirectory.dir("BasicLandMC-Server"))
//        }
//
//        patchTasks.register("generatedApi") {
//            isBareDirectory = true
//            upstreamDirPath = "paper-api-generator/generated"
//            patchDir = layout.projectDirectory.dir("patches/generated-api")
//            outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
//        }
//    }
//}

paperweight {
    upstreams.register("divinemc") {
        repo = github("DivineMC", "DivineMC")
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
            /*
            maven("https://repo.papermc.io/repository/maven-snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
             */
        }
    }
}
