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
    with(libs) {

        //    #   Kotlin
        implementation(kotlin.serialization)

        //    #   Datastore
        implementation(datastore.preferences.core)

        //    #   Koin
        implementation(koin.core)
        implementation(koin.ktor)
    }
}