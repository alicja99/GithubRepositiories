package com.coddeaddict.githubrepositories.model.commits

import com.google.gson.annotations.SerializedName

data class Commit(
    val author: Author,
    @SerializedName ("comment_count")
    val commentCount: Int,
    val committer: Committer,
    val message: String,
    val url: String,
)