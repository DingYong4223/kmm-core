import DevHelper.isBuildAndroidOnly
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension.CocoapodsDependency.PodLocation
import java.net.URI

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.google.devtools.ksp")
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
                    export(project("${extra.get("kmm.lct")}:khttp"))
                    export(project("${extra.get("kmm.lct")}:kmmkv"))
                    export(project("${extra.get("kmm.lct")}:kredux"))
                    export(project("${extra.get("kmm.lct")}:kutils"))
                    export(project("${extra.get("kmm.lct")}:kloger"))
                    export(project("${extra.get("kmm.lct")}:kfile"))
                }
            }
        }
        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
            compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
        }
        cocoapods {
            specRepos {
                url("https://github.com/CocoaPods/Specs")
            }
            summary = "Some description for the shared Module"
            homepage = "Link to the shared Module homepage"
            ios.deploymentTarget = Versions.VERSION_IOS_DEPLOYMENTTARGET
            pod(name = "MMKV", version = Versions.VERSION_MMKV, path = null, moduleName = "MMKV", headers = null)
            pod("KNetwork") {
                this.source = PodLocation.Git(
                    URI(Const.URL_KNETWORK), null, null, null
                )
            }
            framework {
                baseName = "shared"
            }
        }
    }
    
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("${project.buildDir}/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                api(project("${extra.get("kmm.lct")}:khttp"))
                api(project("${extra.get("kmm.lct")}:kmmkv"))
                api(project("${extra.get("kmm.lct")}:kredux"))
                api(project("${extra.get("kmm.lct")}:kutils"))
                api(project("${extra.get("kmm.lct")}:kloger"))
                api(project("${extra.get("kmm.lct")}:kfile"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {

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