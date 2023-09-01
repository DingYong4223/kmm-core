plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = Versions.COMPILE_SDK_VERSION
    defaultConfig {
        applicationId = "com.example.kmmlibs"
        minSdk = Versions.MIN_SDK_VERSION
        targetSdk = Versions.TARGET_SDK_VERSION
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")

    if (rootProject.name.contains("LocalRef")) {
//        implementation(project(":khttp"))
        implementation(project(":kfile"))
        implementation(project(":kloger"))
        implementation(project(":kmmkv"))
        implementation(project(":kredux"))
//        implementation(project(":kreporter"))
        implementation(project(":kutils"))
        implementation(project(":shared"))
    } else {
        implementation("com.fit.kmm:khttp:${Versions.KMM_BASE_LIBS}")
        implementation("com.fit.kmm:kfile:${Versions.KMM_BASE_LIBS}")
        implementation("com.fit.kmm:kloger:${Versions.KMM_BASE_LIBS}")
        implementation("com.fit.kmm:kmmkv:${Versions.KMM_BASE_LIBS}")
        implementation("com.fit.kmm:kredux:${Versions.KMM_BASE_LIBS}")
        implementation("com.fit.kmm:kreporter:${Versions.KMM_BASE_LIBS}")
        implementation("com.fit.kmm:kutils:${Versions.KMM_BASE_LIBS}")
    }
}