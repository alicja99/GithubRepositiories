package com.coddeaddict.githubrepositories.model.commits

data class CommitsItem(
    val author: Author,
    val comments_url: String,
    val commit: Commit,
    val html_url: String,
    val node_id: String,
    val parents: List<Any>,
    val sha: String,
    val url: String
)