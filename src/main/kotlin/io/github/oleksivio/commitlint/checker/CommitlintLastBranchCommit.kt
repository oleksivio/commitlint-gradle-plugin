package io.github.oleksivio.commitlint.checker

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.DefaultTask

open class CommitlintLastBranchCommit : CommitChecker() {
    override fun Repository.loadCommit(): Iterable<RevCommit> = git().log().setMaxCount(1).call()
}
