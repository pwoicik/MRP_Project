import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"

    id("org.jetbrains.compose") version "1.1.1"
    id("com.squareup.sqldelight") version "1.5.3"
}

group = "com.github.pwoicik"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
dependencies {
    // Compose
    implementation(compose.desktop.currentOs)
    implementation(compose.animation)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.uiTooling)
    implementation(compose.preview)
    implementation(compose.materialIconsExtended)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.0")

    // Decompose
    implementation("com.arkivanov.decompose:decompose:0.5.2")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.5.2")

    // Koin
    implementation("io.insert-koin:koin-core:3.1.5")

    // SqlDelight
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.3")

    // JSON serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

compose.desktop {
    application {
        mainClass = "presentation.MainKt"

        nativeDistributions {
            modules("java.sql")

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))

            targetFormats(TargetFormat.Msi)
            packageName = "MRP Project"
            packageVersion = "0.0.3"

            windows {
                iconFile.set(project.layout.projectDirectory.dir("icons").file("icon.ico"))
                menuGroup = "MRP Project"
            }
        }
    }
}

sqldelight {
    database("MrpDatabase") {
        packageName = "data"
    }
}
