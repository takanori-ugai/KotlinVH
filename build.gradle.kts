import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    java
    id("com.gradleup.shadow") version "9.2.2"
    jacoco
    id("org.jetbrains.dokka") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    id("com.github.jk1.dependency-license-report") version "2.9"
    id("com.github.spotbugs") version "6.4.2"
    id("com.diffplug.spotless") version "8.0.0"
}

group = "com.fujitsu"
version = "0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.9.0")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.+")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    testImplementation("org.wiremock:wiremock:3.13.1")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    compileTestKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    compileTestJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport) // report is always generated after tests run
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "11"
        reports {
            // observe findings in your browser with structure and code snippets
            html.required.set(true)
            // checkstyle like format mainly for integrations like Jenkins
            xml.required.set(true)
            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
            // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
            // with Github Code Scanning
            sarif.required.set(true)
        }
    }

    jacocoTestReport {
        reports {
            xml.required = true
            html.required = false
        }
        dependsOn(test) // tests are required to run before generating the report
    }

    shadowJar {
        manifest {
            attributes["Main-Class"] = "com.fujitsu.labs.virtualhome.MainKt"
        }
        minimize()
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    // point to your custom config defining rules to run, overwriting default behavior
    config.setFrom("$projectDir/config/detekt.yml")
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

spotbugs {
    ignoreFailures.set(true)
}

jacoco {
    toolVersion = "0.8.13"
//    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

spotless {
    java {
        target("src/*/java/**/*.java")
        importOrder()
        removeUnusedImports()

        // Choose one of these formatters.
        googleJavaFormat("1.27.0")
        formatAnnotations()
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}
