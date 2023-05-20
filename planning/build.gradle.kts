@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.detekt.gradle)
    alias(libs.plugins.kover.gradle)

    id("org.jetbrains.dokka")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(parent?.buildDir?.resolve("dokka/planning"))
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
        configureEach {
            includes.from("Module.md")
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    //implementation(libs.kotlix.coroutines)
    api(libs.tuprolog.solve.classic)
    api(libs.tuprolog.unify)

    testImplementation(libs.bundles.kotlin.testing)
    testImplementation(project(":domain"))
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = false
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config = files("${rootDir.path}/config/detekt.yml")
    source = files(kotlin.sourceSets.map { it.kotlin.sourceDirectories })
}
