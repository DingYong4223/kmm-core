import org.gradle.api.JavaVersion

/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: Versions
 * @Author: delanding
 * @Date: 2022/9/26 11:30
 * @Description App config for script.
 * ~~~~~~
 */
object Versions {

    val VERSION_NAME = "1.0.0"
    val VERSION_CODE = 144
    //*******************构建信息结束*******************
    val kotlin_version = "1.9.10"
    // 编译版本相关
    val COMPILE_SDK_VERSION = 30
    val MIN_SDK_VERSION = 21
    val TARGET_SDK_VERSION = 30

    val VERSION_DOKKA = "1.7.20"
    val VERSION_OKIO = "3.2.0"
    val VERSION_MMKV = "1.2.14"
    val VERSION_MOCK = "1.12.2"
    val VERSION_JUNIT = "4.13.2"
    val VERSION_IOS_DEPLOYMENTTARGET = "12.0"

    /********module version config**********/
    val KMM_BASE_LIBS   = "1.3.3"   //当前基础库版本号
}
