package com.ivzb.github_browser.data.star

import com.ivzb.github_browser.util.NetworkUtils
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteStarDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : StarRemoteDataSource {

    override fun isStarred(repo: String): Boolean? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        return retrofit.create<StarAPI>(StarAPI::class.java).isStarred(repo).execute().isSuccessful
    }

    override fun star(repo: String): Boolean? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        return retrofit.create<StarAPI>(StarAPI::class.java).star(repo).execute().isSuccessful
    }

    override fun unstar(repo: String): Boolean? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        return !retrofit.create<StarAPI>(StarAPI::class.java).unstar(repo).execute().isSuccessful
    }
}
