package io.github.oleksivio.commitlint

import io.github.oleksivio.commitlint.checker.Commitlint
import io.github.oleksivio.commitlint.checker.CommitlintScopeAll
import io.github.oleksivio.commitlint.checker.CommitlintScopeBranch
import io.github.oleksivio.commitlint.checker.CommitlintScopeOne
import org.gradle.api.Plugin
import org.gradle.api.Project


class CommitlintPlugin : Plugin<Project> {
    
    override fun apply(project: Project) {

        val gradleBuildFolder = project.buildscript.sourceFile?.parentFile
                ?: throw IllegalStateException("Can't get gradle build folder")

        val scopeProperty: String? = System.getenv("scope")


        project.tasks.create("commitlint", Commitlint::class.java).apply {
            checkingFolder = gradleBuildFolder
            scope = when (scopeProperty) {
                "one" -> CommitlintScopeOne()
                "branch" -> CommitlintScopeBranch()
                "all" -> CommitlintScopeAll()
                else -> CommitlintScopeOne()
            }
        }

    }
}
