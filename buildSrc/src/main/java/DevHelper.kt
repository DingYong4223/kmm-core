import org.gradle.api.Project

/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: DevHelper
 * @Author: delanding
 * @Date: 2022/9/26 11:30
 * @Description develop helper.
 * ~~~~~~
 */
object DevHelper {

    private const val PREFIX = "KmmCore"

    /**
     * 双端编译，本地依赖
     * **/
    fun Project.isAllLocalRef(): Boolean {
        return rootProject.name == "{$PREFIX}AllLocalRef"
    }

    /**
     * android端编译，远程依赖
     * **/
    fun Project.isAndroidRemoteRef(): Boolean {
        return rootProject.name == "{$PREFIX}AndroidRemoteRef"
    }

    /**
     * android端编译，本地依赖
     * **/
    fun Project.isAndroidLocalRef(): Boolean {
        return rootProject.name == "{$PREFIX}AndroidLocalRef"
    }

    /**是否仅构建Android端*/
    fun Project.isBuildAndroidOnly(): Boolean {
        return isAndroidLocalRef() || isAndroidRemoteRef()
    }

}
