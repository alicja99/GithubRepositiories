package com.coddeaddict.githubrepositories.model.commits

import com.google.gson.annotations.SerializedName

data class CommitsItem(
    val author: Author,
    @SerializedName("comments_url")
    val commentsUrl: String,
    val commit: Commit,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("node_id")
    val nodeId: String,
    val parents: List<Any>,
    val sha: String,
    val url: String
)