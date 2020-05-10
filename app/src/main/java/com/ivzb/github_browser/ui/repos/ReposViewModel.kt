package com.ivzb.github_browser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.repo.*
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.ui.Empty
import com.ivzb.github_browser.ui.NoConnection
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class ReposViewModel @Inject constructor(
    private val observeReposUseCase: ObserveReposUseCase,
    private val fetchReposUseCase: FetchReposUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val click = MutableLiveData<Event<Repo>>()
    val repos: LiveData<Event<List<Any>>>
    val searchQuery = MutableLiveData<Event<String>>()

    init {
        repos = observeReposUseCase.observe().map {
            loading.postValue(Event(false))

            val result = it.successOr(listOf(NoConnection)) ?: listOf(Empty)

            Event(
                when {
                    result.isEmpty() -> listOf(Empty)
                    else -> result
                }
            )
        }
    }

    fun getRepos(user: String, type: RepoType) {
        loading.postValue(Event(true))

        Pair(user, type).run {
            observeReposUseCase.execute(this)
            fetchReposUseCase(this)
        }
    }

    fun click(repo: Repo) =
        click.postValue(Event(repo))

    fun search(query: String?) =
        searchQuery.postValue(Event(query ?: ""))
}
