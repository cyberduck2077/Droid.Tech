plugins {
    kotlin("jvm")
    with(libs.plugins) {
        id(kotlinSerialization.get().pluginId)
        id(kotlinSerializationPlugin.get().pluginId)
        id(devtools.get().pluginId)
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    with(libs) {
        //    #   Kotlin
        implementation(kotlin.serialization)

        //    #   Ktor
        implementation(platform(ktor.boom))
        implementation(ktor.client.core)
        implementation(ktor.serialization.jackson)
        implementation(okhttp.okhttp3)
        implementation(ktor.serialization.gson)

        //    #   Room
        implementation(room.common)

        // Logger
        implementation(napier)
    }
}