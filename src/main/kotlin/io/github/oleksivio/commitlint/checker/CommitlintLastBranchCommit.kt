package io.github.oleksivio.commitlint.checker

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit

class CommitlintLastBranchCommit : CommitChecker() {
    override fun Repository.loadCommit(): Iterable<RevCommit> = git().log().setMaxCount(1).call()
}
