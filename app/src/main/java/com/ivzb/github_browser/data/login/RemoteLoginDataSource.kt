package com.ivzb.github_browser.data.login

import com.ivzb.github_browser.model.ui.AccessToken
import com.ivzb.github_browser.util.NetworkUtils
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteLoginDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : LoginDataSource {

    override fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): AccessToken? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<LoginAPI>(LoginAPI::class.java)
            .getAccessToken(clientId, clientSecret, code).execute()

        return response.body()?.asAccessToken()
    }
}