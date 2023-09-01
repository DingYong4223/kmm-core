import DevHelper.isBuildAndroidOnly
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

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
                baseName = "kloger"
            }
        }
    }

    sourceSets {
        val napierVersion = "2.5.0"

        val commonMain by getting {
            dependencies {
                implementation("io.github.aakira:napier:$napierVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.tencent.mars:mars-xlog:1.2.5")
                implementation("org.greenrobot:eventbus:3.2.0")
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

