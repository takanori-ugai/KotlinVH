import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("multiplatform") version "2.3.20"
    kotlin("plugin.serialization") version "2.3.20"
    id("com.gradleup.shadow") version "9.4.0"
    jacoco
    id("org.jetbrains.dokka") version "2.1.0"
    id("org.jetbrains.dokka-javadoc") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("com.github.jk1.dependency-license-report") version "3.1.1"
    id("com.github.spotbugs") version "6.4.8"
    id("com.diffplug.spotless") version "8.4.0"
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.ugaikit"
version = "0.6.1"

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xenable-suspend-function-exporting")
        freeCompilerArgs.add("-Xes-long-as-bigint")
    }
    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        nodejs {
            testTask {
                useMocha {
                    timeout = "10s"
                }
            }
        }
        binaries.executable()
        binaries.library()
        generateTypeScriptDefinitions()
    }
    linuxX64 {
        binaries {
            executable {
                entryPoint = "io.github.ugaikit.vh.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
                implementation("io.ktor:ktor-client-core:3.4.1")
                implementation("io.ktor:ktor-client-content-negotiation:3.4.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.1")
                implementation("io.github.oshai:kotlin-logging:8.0.01")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:3.4.1")
                implementation("ch.qos.logback:logback-classic:1.5.32")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.mockk:mockk:1.14.9")
//                implementation("org.junit:junit-bom:5.12.0")
                implementation("org.junit.jupiter:junit-jupiter:6.0.3")
                runtimeOnly("org.junit.platform:junit-platform-launcher:6.0.3")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:3.4.1")
                // implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")
            }
        }
        val linuxX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:3.4.1")
            }
        }
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "17"
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

    // Shadow jar task usually only works well with JVM plugin or with application plugin.
    // In multiplatform, it might be tricky.
    // However, since we have jvm target and main class is in there, we can try to configure it.
    // But since `shadowJar` task is not registered automatically for KMP probably.

    // Jacoco config
    val jacocoTestReport =
        register<JacocoReport>("jacocoTestReport") {
            reports {
                xml.required.set(true)
                csv.required.set(true)
                // html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
            }
            dependsOn("jvmTest")
            // sourceSets(kotlin.sourceSets.jvmMain) // might need adjustment
            classDirectories.setFrom(files(layout.buildDirectory.dir("classes/kotlin/jvm/main")))
            sourceDirectories.setFrom(files("src/jvmMain/kotlin", "src/commonMain/kotlin"))
            executionData.setFrom(layout.buildDirectory.file("jacoco/jvmTest.exec"))
        }

    // Fix implicit dependency between jsNodeProductionLibraryDistribution and jsProductionExecutableCompileSync
    // This happens because both use the same package directory when both executable and library binaries are configured.
    named("jsNodeProductionLibraryDistribution") {
        dependsOn("jsProductionExecutableCompileSync")
    }
}

ktlint {
    version.set("1.8.0")
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
        exclude("**/ResourceData.kt")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    source.setFrom(
        files(
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/linuxX86Main/kotlin",
            "src/jvmMain/kotlin",
            "src/jvmTest/kotlin",
        ),
    )
    // point to your custom config defining rules to run, overwriting default behavior
    config.setFrom("$projectDir/config/detekt.yml")
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

spotbugs {
    ignoreFailures.set(true)
}

jacoco {
    toolVersion = "0.8.14"
}

spotless {
    java {
        target("src/*/java/**/*.java")
        importOrder()
        removeUnusedImports()

        // Choose one of these formatters.
        googleJavaFormat("1.28.0")
        formatAnnotations()
    }
}

mavenPublishing {
    // Maven Central に公開する場合の設定
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    // ライブラリの座標設定
    coordinates("io.github.ugaikit", "vh", "0.6.1")

    // POM情報（Maven Centralには必須）
    pom {
        name = "KotlinVH"
        description = "Kotlin Multiplatform library for interacting with the Virtual Home API."
        url = "https://github.com/takanori-ugai/KotlinVH"
        inceptionYear.set("2025")
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "takanori-ugai"
                name = "Takanori Ugai"
                email = "ugai.takanori@gmail.com"
            }
        }
        scm {
            connection = "scm:https://github.com/takanori-ugai/KotlinVH.git"
            developerConnection = "scm:https://github.com/takanori-ugai/KotlinVH.git"
            url = "https://github.com/takanori-ugai/KotlinVH"
        }
    }
}
