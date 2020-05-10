package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoResponse
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.model.search.SearchResponse
import com.ivzb.github_browser.model.user.UserResponse
import com.ivzb.github_browser.util.NetworkUtils
import com.ivzb.github_browser.util.checkAllMatched
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteRepoDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : RepoRemoteDataSource {

    override fun getRepos(user: String, type: RepoType): List<Repo>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<RepoAPI>(RepoAPI::class.java).let {
            when (type) {
                RepoType.Own -> it.getOwnRepos(user).execute().body()
                RepoType.Starred -> it.getStarredRepos(user).execute().body()
                RepoType.Search -> it.getSearchRepos(user).execute().body()?.items
            }.checkAllMatched
        }

        return response?.map { it.asRepo() }
    }

    override fun getRepo(repo: String): Repo? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<RepoAPI>(RepoAPI::class.java).getRepo(repo).execute()

        return response.body()?.asRepo()
    }
}
