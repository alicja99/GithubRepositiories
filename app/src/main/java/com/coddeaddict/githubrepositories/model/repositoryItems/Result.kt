package com.coddeaddict.githubrepositories.model.repositoryItems

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Result(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val repositoryItems: List<RepositoryItem>,
    @SerializedName("total_count")
    val totalCount: Int
): Serializable {
    constructor() : this(false, listOf(), 0)
}