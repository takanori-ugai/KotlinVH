# KotlinVH

## Virtual Home client library

[![codecov](https://codecov.io/gh/takanori-ugai/KotlinVH/graph/badge.svg?token=VQ2ST9X123)](https://codecov.io/gh/takanori-ugai/KotlinVH)

This repository contains a Kotlin Multiplatform library for interacting with the Virtual Home API. It supports JVM, Linux (X64), and JavaScript (Node.js). Virtual Home is a multi-agent platform for grounded language learning. You can find more information about Virtual Home on the official website: [http://virtual-home.org/](http://virtual-home.org/)

## Installation

### JVM

To use this library in your JVM project, add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.ugaikit:vh:0.6")
}
```

### Node.js

The library can be compiled to a Node.js module.

```bash
./gradlew jsNodeProductionLibraryDistribution
```

The generated module can be found in `build/compileSync/js/main/productionLibrary/kotlin/`.

## Usage

### Kotlin (JVM)

Here is a simple example of how to use the `VirtualHomeClient`:

```kotlin
import io.github.ugaikit.vh.VirtualHomeClient
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val client = VirtualHomeClient()

    // Reset the environment to scene 0
    client.reset(0)

    // Add a character to the scene
    client.addCharacter()

    // Define a script for the character to execute
    val script = listOf(
        "<char0> [WALK] <tv> (106)",
        "<char0> [TOUCH] <tv> (106)"
    )

    // Render the script
    client.renderScript(script)
}
```

### Node.js

To use the library in a Node.js project, require the generated module. Note that functions are asynchronous and return Promises.

```javascript
const vh = require('./build/compileSync/js/main/productionLibrary/kotlin/VirtualHome.js');
const VirtualHomeClient = vh.io.github.ugaikit.vh.VirtualHomeClient;

async function main() {
    const client = new VirtualHomeClient("localhost", 8080);

    // Reset the environment to scene 0
    await client.reset(0);

    // Add a character
    await client.addCharacter();

    console.log("Setup complete");
}

main();
```

## API Documentation

You can find the API documentation [here](https://ugaigroup1.gitlab.io/kotlinvh/).

## Build from source

To build the library from source, you will need to have Gradle installed. Then, run the following command in the root of the repository:

```bash
./gradlew build
```
