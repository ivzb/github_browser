package com.ivzb.github_browser.ui.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.domain.user.FetchUserUseCase
import com.ivzb.github_browser.domain.user.ObserveUserUseCase
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val observeUserUseCase: ObserveUserUseCase,
    private val fetchUserUseCase: FetchUserUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val error = MutableLiveData<Event<String>>()
    val user: LiveData<Event<User?>>
    val userProfileEvent: MutableLiveData<Event<Pair<UserProfileEvent, String>>> = MutableLiveData()

    init {
        user = observeUserUseCase.observe().map {
            loading.postValue(Event(false))

            Event(it.successOr(null).also { user -> sendError(user) })
        }
    }

    fun getUser(user: String?) {
        loading.postValue(Event(true))

        observeUserUseCase.execute(user)
        fetchUserUseCase(user)
    }

    fun ownReposClick(user: String) =
        emitEvent(UserProfileEvent.Repositories, user)

    fun starredReposClick(user: String) =
        emitEvent(UserProfileEvent.Starred, user)

    fun followingClick(user: String) =
        emitEvent(UserProfileEvent.Following, user)

    fun followersClick(user: String) =
        emitEvent(UserProfileEvent.Followers, user)

    private fun emitEvent(event: UserProfileEvent, user: String) =
        userProfileEvent.postValue(Event(Pair(event, user)))

    private fun sendError(user: User?) =
        error.postValue(
            when (user) {
                null -> Event(COULD_NOT_GET_USER)
                else -> null
            }
        ).checkAllMatched

    companion object {
        const val COULD_NOT_GET_USER = "Couldn't get user. Please try again."
    }
}

enum class UserProfileEvent {
    Repositories,
    Starred,
    Following,
    Followers
}
