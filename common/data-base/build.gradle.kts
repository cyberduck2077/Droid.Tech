import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    with(libs.plugins) {
        id(library.get().pluginId)
        id(kotlinAndroid.get().pluginId)
        id(devtools.get().pluginId)
    }
}

android{
    namespace = "ru.data.common.db"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":common:data-models"))
    with(libs) {

        //    #   Room
        implementation(room.ktx)
        implementation(room.runtime)
        ksp(room.compiler)

        //    #   Koin
        runtimeOnly(koin.androidx.compose)
        implementation(koin.android)
        implementation(koin.core)
        implementation(koin.ktor)

    }
}