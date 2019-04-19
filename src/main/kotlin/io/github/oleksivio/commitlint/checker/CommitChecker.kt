package io.github.oleksivio.commitlint.checker

import io.github.oleksivio.commitlint.CommitlintPlugin
import io.github.oleksivio.commitlint.exceptions.InvalidCommitMessageFormatException
import io.github.oleksivio.commitlint.exceptions.InvalidCommitMessageValueException
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.revwalk.filter.RevFilter
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

abstract class CommitChecker {

    fun checkCommit(checkingFolder: File, checkerType: CommitCheckerType) {

        val repository = FileRepositoryBuilder()
                .findGitDir(checkingFolder)
                .apply {
                    gitDir ?: throw IllegalStateException(
                            "Project must be in a git directory for git-versioning to work." +
                                    "  Recommended solution: git init"
                    )
                }
                .build() ?: throw IllegalStateException("")

        repository.loadCommit().asSequence().map { it.parse() }.forEach { it.check(checkerType) }
    }

    protected fun Repository.git(): Git = Git.wrap(this)
    

    abstract fun Repository.loadCommit(): Iterable<RevCommit>

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
