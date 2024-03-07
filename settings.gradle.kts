rootProject.name = "XAIP-lib"
include("explanation")
include("planning")
include("dsl")
include("domain")
include("evaluation")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.23"
        id("org.jetbrains.dokka") version ("1.9.20")
    }
}
