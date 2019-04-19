package io.github.oleksivio.commitlint

import io.github.oleksivio.commitlint.checker.CommitCheckerType

open class CommitlintPluginExtension {
    var type: CommitCheckerType = CommitCheckerType.DEFAULT
}