package com.ivzb.github_browser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.repo.*
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.ui.Empty
import com.ivzb.github_browser.ui.NoConnection
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class ReposViewModel @Inject constructor(
    private val getOwnReposUseCase: GetOwnReposUseCase,
    private val getStarredReposUseCase: GetStarredReposUseCase,
    private val getSearchReposUseCase: GetSearchReposUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val click = MutableLiveData<Event<Repo>>()
    val repos: LiveData<List<Any>>
    val searchQuery = MutableLiveData<String>()

    private val getReposResult = MutableLiveData<Result<List<Repo>?>>()

    init {
        repos = getReposResult.map {
            loading.postValue(Event(false))

            val result = it.successOr(listOf(NoConnection)) ?: listOf(Empty)

            when {
                result.isEmpty() -> listOf(Empty)
                else -> result
            }
        }
    }

    fun getRepos(user: String, type: RepoType) {
        loading.postValue(Event(true))

        when (type) {
            RepoType.Own -> getOwnReposUseCase(user, getReposResult)
            RepoType.Starred -> getStarredReposUseCase(user, getReposResult)
            RepoType.Search -> getSearchReposUseCase(user, getReposResult)
        }.checkAllMatched
    }

    fun click(repo: Repo) {
        click.postValue(Event(repo))
    }

    fun search(query: String?) {
        searchQuery.postValue(query ?: "")
    }
}
