buildscript {
    apply(from = "${rootDir}/buildSrc/gradle/utils.gradle")
    apply(from = "${rootDir}/buildSrc/gradle/config.gradle")
    repositories {
        KtsHelper.injectProjectRepo(this) {
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}")
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${Versions.VERSION_DOKKA}")
    }
}

allprojects {
    repositories {
        KtsHelper.injectProjectRepo(this) {
        }
    }
    group = property("GROUP").toString()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

repositories {
    mavenCentral()
}

plugins {
    //kotlin("jvm") version "1.7.0" apply false
    id("org.jetbrains.dokka") version Versions.VERSION_DOKKA
    id("com.vanniktech.maven.publish") version "0.22.0" apply false
}
tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve(".docs"))
}
