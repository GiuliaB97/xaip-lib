rootProject.name = "XAIP-lib"
include("explanation")
include("planning")
include("dsl")
include("domain")
include("evaluation")

pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
        id("org.jetbrains.dokka") version ("1.9.20")
    }
}
