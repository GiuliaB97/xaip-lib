@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.kover.gradle)
    id("org.jetbrains.dokka")
    application
    alias(libs.plugins.javafx)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("gh_pages/dsl"))
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
        configureEach {
            includes.from("Module.md")
        }
    }
}
dependencies {
    api(libs.tuprolog.unify)
    api(libs.tuprolog.solve.classic)

    implementation(libs.kotlin.stdlib)
    testImplementation(libs.bundles.kotlin.testing)

    api(project(":planning"))
    testImplementation(project(":domain"))
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
        }
    }
}

@Suppress("SpreadOperator")
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

javafx {
    modules("javafx.base", "javafx.controls", "javafx.fxml", "javafx.graphics")
}
