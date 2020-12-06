package com.coddeaddict.githubrepositories.viewmodel.repodetails

import androidx.lifecycle.ViewModel
import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RepoDetailsViewModel (private val githubRepository: GithubRepository): ViewModel() {

}