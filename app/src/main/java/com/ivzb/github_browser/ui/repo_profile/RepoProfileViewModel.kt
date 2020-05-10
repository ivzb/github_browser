package com.ivzb.github_browser.ui.repo_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.repo.GetRepoUseCase
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class RepoProfileViewModel @Inject constructor(
    private val getRepoUseCase: GetRepoUseCase
): ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val error = MutableLiveData<Event<String>>()
    val repo: LiveData<Repo?>
    val repoProfileEvent: MutableLiveData<Event<Pair<RepoProfileEvent, String>>> = MutableLiveData()

    private val getRepoResult = MutableLiveData<Result<Repo?>>()

    init {
        repo = getRepoResult.map {
            loading.postValue(Event(false))

            it.successOr(null).also { repo ->
                error.postValue(when (repo) {
                    null -> Event(COULD_NOT_GET_REPO)
                    else -> null
                })
            }
        }
    }

    fun getRepo(repo: String) {
        loading.postValue(Event(true))

        getRepoUseCase(repo, getRepoResult)
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

    companion object {
        const val COULD_NOT_GET_REPO = "Couldn't get repo. Please try again."
    }
}

enum class RepoProfileEvent {
    Contributors,
    Owner
}
