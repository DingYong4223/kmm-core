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
        val iosX64 = iosX64()
        val iosArm64 = iosArm64()
        val iosSimulatorArm64 = iosSimulatorArm64()
        targets {
            configure(listOf(iosX64, iosArm64, iosSimulatorArm64)) {
                binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java) {
                }
            }
        }
        cocoapods {
            summary = "Some description for the kmmkv Module"
            homepage = "Link to the kmmkv Module homepage"
//            specRepos {
//                url("https://github.com/CocoaPods/Specs")
//                //url("https://mirrors.tuna.tsinghua.edu.cn/git/CocoaPods/Specs.git")
//                url("https://cdn.cocoapods.org/")
//            }
            ios.deploymentTarget = Versions.VERSION_IOS_DEPLOYMENTTARGET
            pod("MMKV", Versions.VERSION_MMKV)
            //podfile = project.file("${extra.get("lctIosProjPodfile")}")
            framework {
                baseName = "kmmkv"
            }
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project("${extra.get("kmm.lct")}:kutils"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.tencent:mmkv:${Versions.VERSION_MMKV}")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:${Versions.VERSION_JUNIT}")
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