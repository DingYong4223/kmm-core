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
        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
            compilations.get("main").kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
        }
        cocoapods {
            summary = "Some description for the Shared Module"
            homepage = "Link to the Shared Module homepage"
            ios.deploymentTarget = Versions.VERSION_IOS_DEPLOYMENTTARGET
            //podfile = project.file("${extra.get("lctIosProjPodfile")}")
            framework {
                baseName = "kredux"
            }
        }
    }
    
    sourceSets {
        val coroutinesVersion = "1.6.1"
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
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