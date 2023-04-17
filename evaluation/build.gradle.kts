@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.detekt.gradle)
    alias(libs.plugins.kover.gradle)
    application
    id("org.jetbrains.dokka")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("gh_pages/evaluation"))
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
    implementation("io.kotest:kotest-runner-junit5-jvm:5.6.0")
    implementation("junit:junit:4.13.2")
    testImplementation(libs.bundles.kotlin.testing)
    api(project(":planning"))
    api(project(":explanation"))
    api(project(":dsl"))
    api(project(":domain"))
}

application {
    mainClass.set("utils.MainKt")
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

tasks.register<Exec>("generateCharts") {
    workingDir("src/main/python")
    commandLine("python", "createplots.py")
    dependsOn("installPythonDependencies")
    dependsOn("run")
}

tasks.register<Exec>("installPythonDependencies") {
    workingDir(rootDir.path) // directory containing the requirements.txt file
    commandLine("python", "-m", "pip", "install", "-r", "requirements.txt")
}
