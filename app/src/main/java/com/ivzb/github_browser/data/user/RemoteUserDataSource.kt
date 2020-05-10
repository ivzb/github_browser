package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.user.User
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

    override fun getUser(user: String): User? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).getUser(user).execute()

        return response.body()?.asUser()
    }

    override fun getFollowing(user: String): List<User>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).getFollowing(user).execute()

        return response.body()?.map { it.asUser() }
    }

    override fun getContributors(repo: String): List<User>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).getContributors(repo).execute()

        return response.body()?.map { it.asUser() }
    }

    override fun getFollowers(user: String): List<User>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).getFollowers(user).execute()

        return response.body()?.map { it.asUser() }
    }

    override fun getSearchUsers(query: String): List<User>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).getSearchUsers(query).execute()

        return response.body()?.items?.map { it.asUser() }
    }
}
