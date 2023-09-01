import DevHelper.isBuildAndroidOnly
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension.CocoapodsDependency.PodLocation
import java.net.URI

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

val VERSION_KTOR = "2.0.0"
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
//            pod("Alamofire", "~> 4.9")
//            pod("KNetwork", path = File(project.rootDir,"/PodKmm/KNetwork"))
//            pod("KNetwork", path = File("/Users/yongding/ProjDoing/tenpay/ThirdParty-IOS2KMM/KNetwork"))
            pod("KNetwork") {
                this.source = PodLocation.Git(
                    URI(Const.URL_KNETWORK), null, null, null
                )
            }
            framework {
                baseName = "khttp"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$VERSION_KTOR")
                implementation("io.ktor:ktor-client-logging:$VERSION_KTOR")
                implementation("io.ktor:ktor-client-serialization:$VERSION_KTOR")

                implementation(project("${extra.get("kmm.lct")}:kutils"))
//                implementation("com.fit.kmm:kjson:${Versions.VERSION_KJSON}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                //implementation("io.ktor:ktor-client-android:$ktor_version")
                implementation("io.ktor:ktor-client-okhttp:$VERSION_KTOR")
            }
        }
        val androidUnitTest by getting

        if (!isBuildAndroidOnly()) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                dependencies {
                    //implementation("io.ktor:ktor-client-darwin:$VERSION_KTOR") //only support ios13 or higher
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
