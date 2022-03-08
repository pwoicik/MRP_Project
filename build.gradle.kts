import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
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
    testImplementation(kotlin("test"))

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
}

tasks.test {
    useJUnitPlatform()
}

val compilerArgs = listOfNotNull(
    "-opt-in=kotlin.RequiresOptIn",
    "-Xopt-in=kotlin.RequiresOptIn"
)

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = compilerArgs
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "MRP_Project"
            packageVersion = "0.0.1"
        }
    }
}
