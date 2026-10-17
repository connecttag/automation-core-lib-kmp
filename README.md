# ConnectTag Automation Core (KMP)

[![](https://jitpack.io/v/connecttag/automation-core-lib-kmp.svg)](https://jitpack.io/#connecttag/automation-core-lib-kmp)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A powerful, high-throughput Event-Driven Automation and Rules Engine for Kotlin Multiplatform (Android & JVM).

Designed for reactive workflows, triggers, conditions evaluation, action execution, and deterministic scheduling.

## Features

- 🌐 **100% Kotlin Multiplatform**: Android & JVM targets.
- ⚡ **Event-Driven Rules Evaluator**:
  - `GlobalConstraintsEvaluator`: Evaluates prerequisite system states (battery, network type, time windows).
  - `NetworkRuleMatcher`: Matches domain names, IP subnets, ports, and protocols against active automation policies.
- ⏱️ **Rate Limiting & Cooldowns**:
  - `ActionRateLimiter`: Enforces per-action execution quotas and sliding window rate limits.
  - `ActionCooldown`: Manages exponential backoff and snooze timeouts.
- 📅 **Deterministic Scheduling**:
  - `AutomationNextRunCalculator`: Accurately computes recurrent intervals and cron-like triggers.

## Installation

Add JitPack repository to your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}
```

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.connecttag:automation-core-lib-kmp:1.0.0")
}
```

## License

```
Copyright 2026 Connect Tag

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
