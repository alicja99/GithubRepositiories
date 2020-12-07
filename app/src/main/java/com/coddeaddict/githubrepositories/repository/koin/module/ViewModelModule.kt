package com.coddeaddict.githubrepositories.repository.koin.module

import com.coddeaddict.githubrepositories.viewmodel.repodetails.RepoDetailsViewModel
import com.coddeaddict.githubrepositories.viewmodel.repolist.RepoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val repoListViewModelModule = module {
    viewModel {
        RepoListViewModel(get())
    }
}

@OptIn(KoinApiExtension::class)
val repoDetailsViewModelModule = module {
    viewModel {
        RepoDetailsViewModel(get())
    }
}

val viewModelModules = listOf(repoListViewModelModule, repoDetailsViewModelModule)