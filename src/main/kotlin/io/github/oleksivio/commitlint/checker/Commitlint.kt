package io.github.oleksivio.commitlint.checker

import io.github.oleksivio.commitlint.exceptions.InvalidCommitMessageFormatException
import io.github.oleksivio.commitlint.exceptions.InvalidCommitMessageValueException
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class Commitlint : DefaultTask() {

    @Input
    var checkType: CommitCheckerType = CommitCheckerType.DEFAULT

    @Input
    var checkingFolder: File = File(".")

    @Input
    var scope: CommitlintScope = CommitlintScopeOne()

    @TaskAction
    fun checkCommit() {

        val repository = FileRepositoryBuilder()
                .findGitDir(checkingFolder)
                .apply {
                    gitDir ?: throw IllegalStateException(
                            "Project must be in a git directory for git-versioning to work." +
                                    "  Recommended solution: git init"
                    )
                }
                .build() ?: throw IllegalStateException("")



        scope.loadCommit(repository).asSequence().map { it.parse() }.forEach { it.check(checkType) }
    }


    data class ParseResult(val type: String, val scope: String?, val subject: String)

    private fun RevCommit.parse(): ParseResult {
        val message = this.shortMessage
        val delimiterIndex = message.indexOf(":")

        if (delimiterIndex < 0) {
            throw InvalidCommitMessageFormatException(
                    "Commit $id not contain ':' symbol. " +
                            "Commit type must fully comply with conventional commit format: " +
                            "'type(scope?): subject  #scope is optional' "
            )
        }

        val subject = message.substring(delimiterIndex)

        val firstPart = message.substring(0, delimiterIndex)
        val scopeIndex = firstPart.indexOf("(")

        val scope = if (scopeIndex > 0) {
            firstPart.substring(scopeIndex)
        } else {
            null
        }

        val type = if (scopeIndex > 0) {
            firstPart.substring(0, scopeIndex)
        } else {
            firstPart
        }

        return ParseResult(type, scope, subject)
    }


    private fun ParseResult.check(commitCheckerType: CommitCheckerType) {
        if (commitCheckerType.typeList.none { it == type }) {
            throw InvalidCommitMessageValueException("Unknown commit type")
        }
    }
}
