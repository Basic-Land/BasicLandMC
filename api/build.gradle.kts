plugins {
    id("asp.base-conventions")
    id("asp.publishing-conventions")
}

group = "com.infernalsuite.asp"

dependencies {
    api(libs.annotations)
    api(libs.adventure.nbt)

    compileOnly(paperApi())
}

publishConfiguration {
    name = "Advanced Slime Paper API"
    description = "API for Advanced Slime Paper"
}
