package com.coddeaddict

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.coddeaddict.githubrepositories.repository.koin.module.repositoryModule
import com.coddeaddict.githubrepositories.repository.koin.module.viewModelModules
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

    private val moduleList = listOf(repositoryModule, retrofitModule) + viewModelModules
}