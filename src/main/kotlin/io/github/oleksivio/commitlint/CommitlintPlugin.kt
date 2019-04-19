package io.github.oleksivio.commitlint

import io.github.oleksivio.commitlint.checker.CommitlintAllCommit
import io.github.oleksivio.commitlint.checker.CommitlintLastBranchCommit
import io.github.oleksivio.commitlint.checker.CommitlintUniqueBranchCommit
import org.gradle.api.Plugin
import org.gradle.api.Project


class CommitlintPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create("commitlint", CommitlintPluginExtension::class.java)

        val gradleBuildFolder = project.buildscript.sourceFile?.parentFile
                ?: throw IllegalStateException("Can't get gradle build folder")

        project.task("commitlintLastCommit") { task ->
            task.doLast {
                CommitlintLastBranchCommit().checkCommit(gradleBuildFolder, extension.type)
            }
        }

        project.task("commitlintUniqueBranchCommit") { task ->
            task.doLast {
                CommitlintUniqueBranchCommit().checkCommit(gradleBuildFolder, extension.type)
            }
        }

        project.task("commitlintAll") { task ->
            task.doLast {
                CommitlintAllCommit().checkCommit(gradleBuildFolder, extension.type)
            }
        }
    }
}
