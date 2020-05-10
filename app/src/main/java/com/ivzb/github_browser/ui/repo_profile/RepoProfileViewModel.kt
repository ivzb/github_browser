package com.ivzb.github_browser.ui.repo_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.repo.FetchRepoUseCase
import com.ivzb.github_browser.domain.repo.ObserveRepoUseCase
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class RepoProfileViewModel @Inject constructor(
    private val observeRepoUseCase: ObserveRepoUseCase,
    private val fetchRepoUseCase: FetchRepoUseCase
) : ViewModel() {

    val loading: MutableLiveData<Event<Boolean>>
    val error = MutableLiveData<Event<String>>()
    val repo: LiveData<Event<Repo?>>
    val repoProfileEvent: MutableLiveData<Event<Pair<RepoProfileEvent, String>>> = MutableLiveData()

    private val fetchRepoResult = MutableLiveData<Result<Boolean>>()

    init {
        repo = observeRepoUseCase.observe().map {
            Event(it.successOr(null))
        }

        loading = fetchRepoResult.map { fetched ->
            fetched.successOr(false).also { sendError(it) }
            Event(false)
        } as MutableLiveData<Event<Boolean>>
    }

    fun getRepo(repo: String) {
        loading.postValue(Event(true))
        observeRepoUseCase.execute(repo)
        fetchRepoUseCase(repo, fetchRepoResult)
    }

    fun star(repo: Repo) {
        // todo
    }

    fun unstar(repo: Repo) {
        // todo
    }

    fun contributorsClick(repo: String) =
        emitEvent(RepoProfileEvent.Contributors, repo)

    fun ownerClick(user: String) =
        emitEvent(RepoProfileEvent.Owner, user)

    private fun emitEvent(event: RepoProfileEvent, value: String) =
        repoProfileEvent.postValue(Event(Pair(event, value)))

    private fun sendError(fetched: Boolean) =
        error.postValue(
            when (fetched) {
                false -> Event(COULD_NOT_GET_REPO)
                else -> null
            }.checkAllMatched
        )

    companion object {
        const val COULD_NOT_GET_REPO = "No internet connection.\nCouldn't get repo.\nPlease try again."
    }
}

enum class RepoProfileEvent {
    Contributors,
    Owner
}
