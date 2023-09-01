/*******配置开发模式 BEGIN********/
rootProject.name = "KmmCoreAllLocalRef"           //Android+IOS构建，Demo使用local依赖（IOS开发，发布）
//rootProject.name = "KmmCoreAndroidLocalRef"     //Android，Demo使用local依赖（本地调试）
//rootProject.name = "KmmCoreAndroidRemoteRef"    //Android，Demo使用remote依赖
/*******配置开发模式 END********/

pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings
    plugins {
        id("com.google.devtools.ksp") version kspVersion apply false
        //kotlin("multiplatform") version kotlinVersion apply false
    }
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(":androidApp")
if (rootProject.name.contains("LocalRef")) {
    include(":kloger")
    include(":khttp")
    include(":kredux")
    include(":kutils")
    include(":kmmkv")
    include(":kfile")
    include(":shared")
}
