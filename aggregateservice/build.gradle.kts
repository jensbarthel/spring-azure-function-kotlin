import Build_gradle.Stage.Companion.toStage
import com.microsoft.azure.gradle.configuration.GradleRuntimeConfig

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:[2.7.3,3.0)")
        classpath("org.jetbrains.kotlin:kotlin-noarg:1.7.10")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.7.10")
    }
}

plugins {
    kotlin("plugin.spring") version "1.7.10"
    id("com.microsoft.azure.azurefunctions") version "[1.10.0,2.0)"
}

val springCloudVersion = "3.2.0"
val springBootVersion = "[2.7.3,3.0)"

dependencies {
    implementation(platform("org.springframework.cloud:spring-cloud-function-dependencies:$springCloudVersion"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation("com.microsoft.azure.functions:azure-functions-java-library")
    implementation("org.springframework.cloud:spring-cloud-function-adapter-azure")
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")
    compileOnly("org.springframework.cloud:spring-cloud-starter-function-web")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.github.jensbarthel.safk.aggregateservice.app.AggregateServiceApp"
    }
}

azurefunctions {
    val stage = System.getenv("STAGE")?.toStage() ?: Stage.DEV

    resourceGroup = stage.resourceGroup
    appName = stage.appname
    pricingTier = "Consumption"
    region = "westeurope"
    localDebug = "transport=dt_socket,server=y,suspend=n,address=5005"
    setRuntime(
        closureOf<GradleRuntimeConfig> {
            os("Linux")
            javaVersion("11")
        }
    )
    setAppSettings(
        closureOf<MutableMap<String, String>> {
            put("SPRING_PROFILES_ACTIVE", stage.internalProfile)
            put("ApplicationInsightsAgent_EXTENSION_VERSION", "~4")
            put("MAIN_CLASS", "com.github.jensbarthel.safk.aggregateservice.app.AggregateServiceApp")
        }
    )
}

enum class Stage(val resourceGroup: String, val internalProfile: String) {
    DEV("YourCrazyResourceGroupDev", "dev"),
    QA("YourCrazyResourceGroupQa", "qa"),
    PROD("YourCrazyResourceGroupProd", "prod");

    val appname get() = "account-$internalProfile"

    companion object {
        fun String.toStage() = values().first { it.name == this.toUpperCase() }
    }
}
