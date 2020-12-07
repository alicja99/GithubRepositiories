package com.coddeaddict.githubrepositories.model.repositoryItems

import java.io.Serializable

data class RepositoryItem(
    val description: String,
    val full_name: String,
    val id: Int,
    val name: String,
    val owner: Owner,
    val stargazers_count: Int,
    val url: String,
) : Serializable {
    constructor() : this(
        "",
        "",0,"",
        Owner("", 0, "", ""),
        0,
        ""
    )
}