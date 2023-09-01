import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KmmHelper
 * @Author: delanding
 * @Date: 2022/10/14 16:30
 * @Description this file only for android project
 * ~~~~~~
 */
object KmmHelper {

    /**
     * inject source set to compile set
     * @param project the project of the module.
     * @param path the path which will be injected.
     * */
    fun injectSource(project: Project, path: String) {}

    /**
     * 获取项目配置boolean属性
     * @param proj project of the kmm.
     * @param key key of the property declared in gradle.property.
     * @param def default value if has no.
     * */
    fun getPropertyBoolean(proj: Project, key: String, def: Boolean): Boolean {
        return if(proj.hasProperty(key)) proj.property(key).toString().toBoolean() else def
    }

    /**
     * 获取项目配置string属性
     * @param proj project of the kmm.
     * @param key key of the property declared in gradle.property.
     * */
    fun getPropertyString(proj: Project, key: String): String {
        return if(proj.hasProperty(key)) proj.property(key).toString() else ""
    }

    /**
     * 注入模块的公共设置属性，如源码路径，发布信息
     * @param project project of the kmm.
     * @param version version of the module
     * */
    fun configCommonSetting(project: Project, version: String) {
        //新增源码路径，在高版本的AS中防止代码爆红
        KmmHelper.injectSource(project, "${project.buildDir}/../src/commonMain/kotlin")

        //发布插件封装
        project.apply(plugin = "com.vanniktech.maven.publish")
        RepoHelper.injectPublishing(project, version)
    }

}
