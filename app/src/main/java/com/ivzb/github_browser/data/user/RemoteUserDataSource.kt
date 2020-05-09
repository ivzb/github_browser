package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.ui.User
import com.ivzb.github_browser.util.NetworkUtils
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : UserDataSource {

    override fun getCurrentUser(): User? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).getCurrentUser().execute()

        return response.body()?.asUser()
    }
}