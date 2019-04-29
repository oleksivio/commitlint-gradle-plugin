package io.github.oleksivio.commitlint.checker

import org.eclipse.jgit.api.LogCommand
import org.eclipse.jgit.lib.AnyObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit

open class CommitlintUniqueBranchCommit : CommitChecker() {
    override fun Repository.loadCommit(): Iterable<RevCommit> {
        val currentBranchRef = resolve(fullBranch)

        val otherBranchesRef = git().branchList().call()
                .map { it.name }
                .filter { it != fullBranch }
                .map { resolve(it) }


        return git().log()
                .exclude(otherBranchesRef)
                .add(currentBranchRef)
                .call()

    }

    private fun LogCommand.exclude(excludeBranches: List<AnyObjectId>): LogCommand {
        excludeBranches.forEach { this.not(it) }
        return this
    }

}
