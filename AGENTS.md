# Instructions for Agents

This file contains instructions for AI agents working on this repository.

## Project Overview
This is a Kotlin Multiplatform Project (KMP) targeting JVM, Linux X64, and JavaScript (Node.js). The library facilitates interaction with VirtualHome.

- **Package Name:** `io.github.ugaikit.vh`
- **Build System:** Gradle

## Code Structure
- **Common:** `src/commonMain/kotlin/io/github/ugaikit/vh`
- **JVM:** `src/jvmMain/kotlin/io/github/ugaikit/vh`
- **JS:** `src/jsMain/kotlin/io/github/ugaikit/vh`
- **Linux X64:** `src/linuxX64Main/kotlin/io/github/ugaikit/vh`
- **Tests:** `src/commonTest` (Multiplatform), `src/jvmTest` (JVM-specific)

## Build & Test Commands
- **Run Static Analysis:** `./gradlew detekt` (Use `--rerun-tasks` to ignore cache)
- **Format Code:** `./gradlew ktlintFormat`
- **Run JVM Tests:** `./gradlew jvmTest`
- **Run JS Node Tests:** `./gradlew jsNodeTest`
- **Compile JVM:** `./gradlew jvmMainClasses`
- **Build JS Distribution:** `./gradlew jsNodeProductionLibraryDistribution`

## Coding Conventions & Guidelines

### General
- **File I/O:** Use `kotlinx-io` (version 0.6.0) instead of `java.io.File` for multiplatform compatibility.
- **Detekt:**
  - Fix `MagicNumber` warnings.
  - Ignore `TooManyFunctions` and `LongMethod` warnings.
- **Ktlint:** `ResourceData.kt` is explicitly excluded from checks to handle large string constants.
- **VirtualHomeClient:** Do not remove functions from `VirtualHomeClient.kt`. `VirtualHomeClient` and `sendRequest` are `open`.

### Kotlin/JS Interoperability
- **Annotations:** Use `@JsExport` for classes intended for JS consumption.
- **Collections:**
  - Kotlin `MutableList` does not map directly to JS arrays.
  - To modify from JS: `collection.asJsArrayView().push(item)`.
  - To pass JS arrays to Kotlin: `vh.kotlin.collections.KtList.fromJsArray(jsArray)`.
- **TypeScript:**
  - Definitions are generated via `./gradlew jsNodeProductionLibraryDistribution`.
  - No `package.json` or `tsconfig.json` are provided for consumers (minimal setup).
  - Use `InstanceType<typeof ClassName>` in TypeScript for exported Kotlin classes.

### Java Interoperability
- **Wrapper:** Use `JavaVirtualHomeClient` in `src/jvmMain` for synchronous Java access (uses `runBlocking`).
- **Helpers:** Use `createRenderParams` in `JavaVirtualHomeClient` for creating data objects with default values.

## Testing
- **Common Tests:** Use `kotlin.test` in `src/commonTest`.
- **JVM Tests:** Use MockK and JUnit 5 in `src/jvmTest`.
- **Known Issues:** `RequestTest.kt` in `src/commonTest` uses JVM libraries (MockK), causing compilation issues on other targets. **Keep this file as-is.**

## Environment Variables
- `JULES_SESSION_ID`: Current session ID.

## Examples
- **Java:** `src/jvmMain/java/io/github/ugaikit/vh/MainJava.java`
- **TypeScript:** `main2.ts` (demonstrates `/// <reference lib="..." />` usage)
- **JavaScript:** `client_app.js`, `main.js`
- **HTML:** `example.html`
