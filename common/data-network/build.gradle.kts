plugins {
    kotlin("jvm")
    with(libs.plugins) {
        id(kotlinSerialization.get().pluginId)
        id(kotlinSerializationPlugin.get().pluginId)
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":common:data-models"))
    implementation(project(":common:data-store"))
    with(libs) {

        //    #   Kotlin
        implementation(kotlin.serialization)

        //    #   Ktor
        implementation(platform(ktor.boom))
        implementation(ktor.client.core)
        implementation(ktor.client.cio)
        implementation(ktor.client.logging)
        implementation(ktor.client.negotiation)
        implementation(ktor.serialization.json)
        implementation(ktor.serialization.gson)
        implementation(ktor.serialization.jackson)
        implementation(ktor.utils)

        //    #   Tika
        implementation(tika.parsers)
        implementation(tika.core)

        //    #   Koin
        implementation(koin.core)
        implementation(koin.ktor)

    }
}