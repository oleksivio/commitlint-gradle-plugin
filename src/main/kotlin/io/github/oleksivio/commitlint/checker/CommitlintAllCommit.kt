package io.github.oleksivio.commitlint.checker

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.revwalk.filter.RevFilter

class CommitlintAllCommit : CommitChecker() {
    override fun Repository.loadCommit(): Iterable<RevCommit> = RevWalk(this).use { walk ->
        walk.revFilter = RevFilter.NO_MERGES
        walk.toList()
    }

}
