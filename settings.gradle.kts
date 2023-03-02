rootProject.name = "XAIP-lib"
include("explanation")
include("planning")
include("dsl")
include("domain")
include("evaluation")
include("gui")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.10"
        id("org.jetbrains.dokka") version ("1.7.20")
    }
}
include("gui")
