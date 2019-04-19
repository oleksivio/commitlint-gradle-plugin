package io.github.oleksivio.commitlint.checker

enum class CommitCheckerType {
    ANGULAR {
        override val typeList: List<String>
            get() = listOf(
                    "build",
                    "ci",
                    "chore",
                    "docs",
                    "feat",
                    "fix",
                    "perf",
                    "refactor",
                    "revert",
                    "style",
                    "test"
            )

    },
    DEFAULT {
        override val typeList: List<String>
            get() = listOf(
                    "fix", 
                    "feat",
                    "BREAKING CHANGE"
            )
    };

    abstract val typeList: List<String>
}
