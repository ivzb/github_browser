package com.ivzb.github_browser.data

import com.ivzb.github_browser.data.preference.PreferenceStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val preferenceStorage: PreferenceStorage): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().authorize(preferenceStorage.accessToken))
    }

    private fun Request.authorize(accessToken: String?) =
        accessToken?.let {
            newBuilder()
                .addHeader(HEADER_AUTHORIZATION, accessToken)
                .build()
        } ?: this

    companion object {

        private const val HEADER_AUTHORIZATION = "Authorization"
    }
}
