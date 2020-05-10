package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.util.NetworkUtils
import com.ivzb.github_browser.util.checkAllMatched
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : UserRemoteDataSource {

    override fun getUser(user: String?): User? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).let {
            when (user) {
                null -> it.getCurrentUser()
                else -> it.getUser(user)
            }.checkAllMatched
        }.execute().body()

        return response?.asUser()
    }

    override fun getUsers(user: String, type: UserType): List<User>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<UserAPI>(UserAPI::class.java).let {
            when (type) {
                UserType.Following -> it.getFollowing(user).execute().body()
                UserType.Followers -> it.getFollowers(user).execute().body()
                UserType.Contributors -> it.getContributors(user).execute().body()
                UserType.Search -> it.getSearchUsers(user).execute().body()?.items
            }.checkAllMatched
        }

        return response?.map { it.asUser() }
    }
}
