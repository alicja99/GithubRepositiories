package com.coddeaddict

import android.app.Application
import com.coddeaddict.githubrepositories.repository.koin.module.repoListViewModelModule
import com.coddeaddict.githubrepositories.repository.koin.module.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import retrofitModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(moduleList)
        }
    }

    private val moduleList = listOf(repositoryModule, retrofitModule, repoListViewModelModule)
}