import org.jetbrains.compose.compose

version = "1.0"

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1-rc2"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0") //for JVM platform
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
    implementation(
        "io.github.microutils:kotlin-logging-jvm:2.1.16"
    )
    runtimeOnly("org.slf4j:slf4j-simple:1.7.32")


    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    testImplementation ("org.jetbrains.kotlin:kotlin-test:1.6.10")
    testImplementation ("org.jetbrains.kotlin:kotlin-test-junit:1.6.10")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

