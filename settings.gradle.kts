pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
}

include(
    ":app",
    ":common:data-store",
    ":common:data-models",
    ":common:data-base",
    ":common:data-network",
    ":common:domain",
)}
