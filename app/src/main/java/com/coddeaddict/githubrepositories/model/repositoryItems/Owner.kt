package com.coddeaddict.githubrepositories.model.repositoryItems

import java.io.Serializable

data class Owner(
    val avatar_url: String,
    val id: Int,
    val login: String,
    val url: String
): Serializable {
    constructor() : this("" ,0,"" , "")
}