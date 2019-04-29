package io.github.oleksivio.commitlint

import io.github.oleksivio.commitlint.checker.CommitlintAllCommit
import io.github.oleksivio.commitlint.checker.CommitlintLastBranchCommit
import io.github.oleksivio.commitlint.checker.CommitlintUniqueBranchCommit
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


class CommitlintPlugin : Plugin<Project> {
    
    override fun apply(project: Project) {

        val gradleBuildFolder = project.buildscript.sourceFile?.parentFile
                ?: throw IllegalStateException("Can't get gradle build folder")

        project.tasks.create("commitlintLastCommit", CommitlintLastBranchCommit::class.java).apply {
            checkingFolder = gradleBuildFolder
        }

        project.tasks.create("commitlintUniqueBranchCommit", CommitlintUniqueBranchCommit::class.java).apply {
            checkingFolder = gradleBuildFolder
        }

        project.tasks.create("commitlintAll", CommitlintAllCommit::class.java).apply {
            checkingFolder = gradleBuildFolder
        }

    }
}
