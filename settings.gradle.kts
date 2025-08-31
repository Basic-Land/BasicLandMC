import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "BasicLandMC"

for (name in listOf("basiclandmc-api", "basiclandmc-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}

include(":gradle:platform")
include(":api")
include(":core")
include(":importer")
include(":loaders")
include(":plugin")
include("loaders:mongo-loader")
findProject(":loaders:mongo-loader")?.name = "mongo-loader"
include("loaders:api-loader")
findProject(":loaders:api-loader")?.name = "api-loader"
include("loaders:file-loader")
findProject(":loaders:file-loader")?.name = "file-loader"
include("loaders:mysql-loader")
findProject(":loaders:mysql-loader")?.name = "mysql-loader"
include("loaders:redis-loader")
findProject(":loaders:redis-loader")?.name = "redis-loader"