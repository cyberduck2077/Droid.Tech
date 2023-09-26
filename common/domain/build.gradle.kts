plugins {
    with(libs.plugins) {
        id(library.get().pluginId)
        id(kotlinAndroid.get().pluginId)
    }
}

android{
    namespace = "ru.data.common.domain"
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
    implementation(project(":common:data-store"))
    implementation(project(":common:data-models"))
    implementation(project(":common:data-network"))
    implementation(project(":common:data-base"))
    with(libs) {

        //    #   Ktor
        implementation(platform(ktor.boom))
        implementation(ktor.client.core)
        implementation(ktor.utils)

        //    #   Koin
        runtimeOnly(koin.androidx.compose)
        implementation(koin.android)
        implementation(koin.core)
        implementation(koin.ktor)

    }
}