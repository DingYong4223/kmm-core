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
        //classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.10")
        classpath("com.tencent.vasdolly:plugin:3.0.4")
    }
}

allprojects {
    repositories {
        KtsHelper.injectProjectRepo(this) {
        }
    }

    extra.apply {
        if (!has("kmm.lct")) {
            set("kmm.lct", "")
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
