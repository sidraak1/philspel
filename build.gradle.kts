plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "edu.davis.cs.ecs36c"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}


application {
    mainClass = "edu.davis.cs.ecs36c.MainKt"
}
