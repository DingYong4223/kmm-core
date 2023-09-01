import DevHelper.isBuildAndroidOnly
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.6.10"
}

val VERSION_COROUTINES = "1.6.1"
kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    if (!isBuildAndroidOnly()) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
        cocoapods {
            summary = "Some description for the Shared Module"
            homepage = "Link to the Shared Module homepage"
            ios.deploymentTarget = Versions.VERSION_IOS_DEPLOYMENTTARGET
            //podfile = project.file("${extra.get("lctIosProjPodfile")}")
            framework {
                baseName = "kutils"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION_COROUTINES")
                api(project("${extra.get("kmm.lct")}:kloger"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION_COROUTINES")
                implementation("com.google.code.gson:gson:2.8.9")
//                implementation("com.tencent.mobileqq:Pandora:0.5.0")
                api("com.tencent.vasdolly:helper:3.0.4")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("io.mockk:mockk:${Versions.VERSION_MOCK}")
            }
        }

        if (!isBuildAndroidOnly()) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                dependencies {
                }
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
            }
            val iosX64Test by getting
            val iosArm64Test by getting
            val iosSimulatorArm64Test by getting
            val iosTest by creating {
                dependsOn(commonTest)
                iosX64Test.dependsOn(this)
                iosArm64Test.dependsOn(this)
                iosSimulatorArm64Test.dependsOn(this)
            }
        }
    }
}

android {
    compileSdk = Versions.COMPILE_SDK_VERSION
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.MIN_SDK_VERSION
        targetSdk = Versions.TARGET_SDK_VERSION
    }
    libraryVariants.all {
        sourceSets.forEach {
            it.kotlinDirectories
        }
    }
}

KmmHelper.configCommonSetting(project, Versions.KMM_BASE_LIBS)