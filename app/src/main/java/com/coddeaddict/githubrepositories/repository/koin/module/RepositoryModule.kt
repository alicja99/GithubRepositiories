package com.coddeaddict.githubrepositories.repository.koin.module

import com.coddeaddict.githubrepositories.repository.api.repositories.GithubRepository
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val repositoryModule = module {
    single {
        GithubRepository()
    }
}