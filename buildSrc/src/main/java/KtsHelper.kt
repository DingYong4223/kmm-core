import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: KtsHelper
 * @Author: delanding
 * @Date: 2022/9/26 11:30
 * @Description Life with Passion, Code with Creativity.
 * ~~~~~~
 */
object KtsHelper {

    fun Project.repositories(configuration: RepositoryHandler.() -> Unit) =
        repositories.configuration()
    /**
     * 注入repo依赖
     * @param repo: script repository or project repository.
     * @param appendConfig additional repo action.
     * */
    fun injectProjectRepo(
        repo: RepositoryHandler,
        appendConfig: RepositoryHandler.() -> Unit
    ) {
        repo.mavenLocal()
        repo.google()
        repo.gradlePluginPortal()
        repo.mavenCentral()
        repo.maven(url = "https://jitpack.io")
        repo.appendConfig()
    }

}
