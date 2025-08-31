plugins {
    id("asp.base-conventions")
    id("asp.internal-conventions")
    id("asp.publishing-conventions")
}

group = "com.infernalsuite.asp"

dependencies {
    compileOnly(project(":api"))
    compileOnly(paperApi())
    implementation(libs.zstd)
}

publishConfiguration {
    name = "Advanced Slime Paper Core"
    description = "Core logic for Advanced Slime Paper"
}
