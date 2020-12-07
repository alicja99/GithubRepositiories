package com.coddeaddict.githubrepositories.model.commits

data class Commit(
    val author: Author,
    val comment_count: Int,
    val committer: Committer,
    val message: String,
    val url: String,
)