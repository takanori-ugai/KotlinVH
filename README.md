# KotlinVH

## Virtual Home client library

[![codecov](https://codecov.io/gh/takanori-ugai/KotlinVH/graph/badge.svg?token=VQ2ST9X123)](https://codecov.io/gh/takanori-ugai/KotlinVH)

This repository contains a Kotlin library for interacting with the Virtual Home API. Virtual Home is a multi-agent platform for grounded language learning. You can find more information about Virtual Home on the official website: [http://virtual-home.org/](http://virtual-home.org/)

## Installation

To use this library in your project, add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fujitsu:KotlinVH:0.6")
}
```

## Usage

Here is a simple example of how to use the `VirtualHomeClient`:

```kotlin
import com.fujitsu.labs.virtualhome.VirtualHomeClient

fun main() {
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

## API Documentation

You can find the API documentation [here](https://ugaigroup1.gitlab.io/kotlinvh/).

## Build from source

To build the library from source, you will need to have Gradle installed. Then, run the following command in the root of the repository:

```bash
./gradlew build
```
