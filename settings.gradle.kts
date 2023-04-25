rootProject.name = "XAIP-lib"
include("explanation")
include("planning")
include("dsl")
include("domain")
include("evaluation")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.21"
        id("org.jetbrains.dokka") version ("1.8.10")
    }
}
