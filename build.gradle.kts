plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

group = "org.connecttag.libs.kotlin"
version = "1.0.0"

kotlin {
    jvmToolchain(17)

    android {
        namespace = "org.connecttag.lib.automation.core"
        compileSdk = 36
        minSdk = 24
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.serialization.json)
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("ConnectTag Automation Core (KMP)")
            description.set("Event-driven rules engine, rate limiters, trigger evaluators, and scheduling models for Kotlin Multiplatform")
            url.set("https://github.com/connecttag/automation-core-lib-kmp")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("connecttagye")
                    name.set("Connect Tag")
                    email.set("connecttagye@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/connecttag/automation-core-lib-kmp.git")
                developerConnection.set("scm:git:ssh://git@github.com:connecttag/automation-core-lib-kmp.git")
                url.set("https://github.com/connecttag/automation-core-lib-kmp/tree/main")
            }
        }
    }
}
