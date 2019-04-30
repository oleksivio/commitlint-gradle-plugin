package io.github.oleksivio.commitlint.checker

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.LogCommand
import org.eclipse.jgit.lib.AnyObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.revwalk.filter.RevFilter

private fun Repository.git(): Git = Git.wrap(this)

abstract class CommitlintScope {
    abstract fun loadCommit(repository: Repository): Iterable<RevCommit>
}

class CommitlintScopeAll : CommitlintScope() {
    override fun loadCommit(repository: Repository): Iterable<RevCommit> = RevWalk(repository).use { walk ->
        walk.revFilter = RevFilter.NO_MERGES
        walk.toList()
    }
}

class CommitlintScopeOne : CommitlintScope() {
    override fun loadCommit(repository: Repository): Iterable<RevCommit> = repository.git().log().setMaxCount(1).call()
}

class CommitlintScopeBranch : CommitlintScope() {
    override fun loadCommit(repository: Repository): Iterable<RevCommit> {

        println(repository.fullBranch)

        val currentBranchRef = repository.resolve(repository.fullBranch)

        println(currentBranchRef.name)

        val otherBranchesRef = repository.git().branchList().call()
                .map { it.name }
                .map {
                    println(it)
                    it
                }
                .map { repository.resolve(it) }
                .filter { it.name != currentBranchRef.name }

        println("Exclude:")
        otherBranchesRef.forEach { println(it.name) }


        return repository.git().log()
                .exclude(otherBranchesRef)
                .add(currentBranchRef)
                .call()

    }

    private fun LogCommand.exclude(excludeBranches: List<AnyObjectId>): LogCommand {
        excludeBranches.forEach { this.not(it) }
        return this
    }
}
