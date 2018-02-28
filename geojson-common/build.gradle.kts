import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    id("kotlin-platform-common")
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

dependencies {
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:0.22.1")
    compile("org.jetbrains.kotlin:kotlin-stdlib-common:1.2.20")
}
