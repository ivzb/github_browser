package com.ivzb.github_browser.util

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Checks if a network connection exists.
 */
open class NetworkUtils @Inject constructor(private val context: Context) {

    open fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}