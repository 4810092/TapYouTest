# TapYouTest

A modular Android project demonstrating a clean architecture approach. The codebase is structured into
separate layers (`app`, `data`, and `domain`) to encourage separation of concerns and ease of testing.

## Modules
- **domain** – Pure Kotlin module containing business logic abstractions such as `UseCase` and `Result`.
- **data** – Android library module providing implementations for data sources and `DispatcherProvider`.
- **app** – Application module that wires everything together.

## Development
The project uses the Kotlin DSL for Gradle configuration and leverages coroutines for asynchronous work.
All coroutine dispatchers are provided via an abstraction, allowing for deterministic tests and
flexible execution contexts.

## Requirements
- JDK 11+
- Android Gradle Plugin 8.11.1
- Kotlin 2.0.21

To build all modules:
```bash
./gradlew build
```
