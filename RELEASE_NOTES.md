# Release Notes

## Version 0.6.1

This is the first Kotlin Multiplatform (KMP) release of the Virtual Home API client library.

### Highlights
- **Multiplatform Support:** The library now targets JVM, Linux X64, and JavaScript (Node.js).
- **Package Rename:** The package has been renamed to `io.github.ugaikit.vh` (formerly `com.fujitsu.labs.virtualhome`).
- **File I/O:** `java.io.File` usage has been replaced with `kotlinx-io` to support non-JVM platforms.

### Features
- **Cross-Platform API:** Access the Virtual Home API from Kotlin (Common), Java, Node.js, and Linux native applications.
- **Java Compatibility:** Includes `JavaVirtualHomeClient` wrapper for synchronous usage in Java projects.
- **TypeScript Support:** TypeScript definitions (`.d.ts`) are generated for the JavaScript target, enabling typed usage in TypeScript projects.
- **Embedded Resources:** Resource data is now embedded directly in the library to avoid file system dependencies on different platforms.

### Installation

**JVM (Gradle):**
```kotlin
implementation("io.github.ugaikit:vh:0.6.1")
```

**Node.js:**
Generate the library using Gradle:
```bash
./gradlew jsNodeProductionLibraryDistribution
```
The output is located in `build/compileSync/js/main/productionLibrary/kotlin/`.
