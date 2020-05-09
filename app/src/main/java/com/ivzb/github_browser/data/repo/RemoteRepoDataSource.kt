package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.ui.Repo
import com.ivzb.github_browser.util.NetworkUtils
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteRepoDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : RepoDataSource {

    override fun getRepos(user: String): List<Repo>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<RepoAPI>(RepoAPI::class.java).getRepo(user).execute()

        return response.body()?.map { it.asRepo() }
    }
}
