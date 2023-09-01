import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import java.net.URI

/**
 * ~~~~~~
 * Copyright (C), 2015-2022, tencent
 *
 * @FileName: RepoHelper
 * @Author: delanding
 * @Date: 2022/10/14 16:30
 * @Description this file only for android project
 * ~~~~~~
 */
object RepoHelper {

    fun Project.kpublishing(configure: Action<PublishingExtension>): Unit =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("publishing", configure)

    /**
     * inject publishing dls to script.
     * @param project the project of the module.
     * */
    fun injectPublishing(project: Project, version: String) {
        project.version = version
        project.kpublishing {
            repositories {
                maven {
                    url = URI(project.properties["MAVEN_REPO"].toString())
                    credentials {
                        username = project.properties["publish_username"].toString()
                        password = project.properties["publish_token"].toString()
                    }
                }
            }
        }
    }

}
