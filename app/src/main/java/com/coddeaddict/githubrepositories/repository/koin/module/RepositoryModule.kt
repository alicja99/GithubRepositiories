package com.coddeaddict.githubrepositories.repository.koin.module

import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        GithubRepository()
    }
}