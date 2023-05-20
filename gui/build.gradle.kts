@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.javafx)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt.gradle)
    alias(libs.plugins.kover.gradle)
    id("org.jetbrains.dokka")
    application
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
    // api(libs.tuprolog.unify)
    // api(libs.tuprolog.solve.classic)

    implementation(libs.kotlin.stdlib)
    // implementation(project(mapOf("path" to ":domain")))
    // implementation(project(mapOf("path" to ":explanation")))
    // implementation("io.kotest:kotest-runner-junit5-jvm:5.5.5")
    // implementation("junit:junit:4.13.2")
    testImplementation(libs.bundles.kotlin.testing)

    api(project(":explanation"))
    api(project(":planning"))
    api(project(":domain"))
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

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config = files("${rootDir.path}/config/detekt.yml")
    source = files(kotlin.sourceSets.map { it.kotlin.sourceDirectories })
}
