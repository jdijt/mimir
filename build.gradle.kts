import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.allopen") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkiverse.quinoa:quarkus-quinoa:2.4.0")
    implementation("io.quarkiverse.neo4j:quarkus-neo4j:4.1.0")
    implementation("io.quarkus:quarkus-jacoco")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-oidc")
    //implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-core:0.15.1")
    //implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-ollama:0.15.1")
    //implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-neo4j:0.15.1")
    implementation("io.quarkus:quarkus-rest-kotlin-serialization")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    //Workaround for Quarkus issue: Compiler plugins not passed when live reloading.
    // Adds them to the classpath for this.
    quarkusDev("org.jetbrains.kotlin:kotlin-allopen-compiler-plugin:2.0.0")
    quarkusDev("org.jetbrains.kotlin:kotlin-serialization-compiler-plugin:2.0.0")
}

group = "eu.derfniw"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        javaParameters = true
    }
}

// Workaround for various issues in Quarkus around the live reload of kotlin code.
// Specifically: compiler plugins not being applied.
// Manually applies them.
// See: https://github.com/quarkusio/quarkus/issues/37109
tasks.quarkusDev {
    compilerOptions {
        compiler("kotlin").args(
            listOf(
                "-Xplugin=${configurations.quarkusDev.get().files.find { "kotlin-allopen-compiler-plugin" in it.name }}",
                "-Xplugin=${configurations.quarkusDev.get().files.find { "kotlin-serialization-compiler-plugin" in it.name }}",
                "-P=plugin:org.jetbrains.kotlin.allopen:annotation=jakarta.ws.rs.Path",
                "-P=plugin:org.jetbrains.kotlin.allopen:annotation=jakarta.enterprise.context.ApplicationScoped",
                "-P=plugin:org.jetbrains.kotlin.allopen:annotation=jakarta.persistence.Entity",
                "-P=plugin:org.jetbrains.kotlin.allopen:annotation=io.quarkus.test.junit.QuarkusTest",
            )
        )
    }
}