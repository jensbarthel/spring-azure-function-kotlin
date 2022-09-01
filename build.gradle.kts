import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

buildscript {
    repositories { mavenCentral() }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:[10.0, 11.0)")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:[1.20, 2.0)")
    }
}

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jlleitschuh.gradle.ktlint") version "[10.0, 11.0)"
    id("io.gitlab.arturbosch.detekt") version "[1.20, 2.0)"
}

allprojects {
    group = "com.ndi.account"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        testImplementation("io.kotest:kotest-runner-junit5:5.4.2")
        testImplementation("io.kotest:kotest-assertions-core:5.4.2")
        testImplementation("io.kotest:kotest-property:5.4.2")
        testImplementation("io.kotest:kotest-framework-datatest:5.4.2")
        testImplementation("io.mockk:mockk:1.12.7")
    }

    detekt {
        buildUponDefaultConfig = true
        config = files("$rootDir/detekt.yml")
        baseline = file("$rootDir/config/detekt/baseline.xml")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events(
                PASSED,
                SKIPPED,
                FAILED
            )
            exceptionFormat = FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }

    tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask> { dependsOn("ktlintFormat") }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> { dependsOn("runKtlintFormatOverMainSourceSet") }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
