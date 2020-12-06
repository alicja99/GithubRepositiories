package com.coddeaddict.githubrepositories.repository.koin.module

import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val repositoryModule = module {
    single {
        GithubRepository()
    }
}