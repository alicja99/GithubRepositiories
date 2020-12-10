package com.coddeaddict.githubrepositories.repository.api

import android.content.Context
import android.net.ConnectivityManager
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
       if(!isInternetAvailable()) throw NoInternetException(applicationContext.resources.getString(R.string.check_internet_connection))
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}