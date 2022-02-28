import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.tylerjroach"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}


tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.tylerjroach.MainKt"))
        }
        archiveFileName.value("${archiveBaseName.get()}.${archiveExtension.get()}")
    }
}