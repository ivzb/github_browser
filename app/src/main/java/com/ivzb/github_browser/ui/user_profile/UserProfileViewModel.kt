package com.ivzb.github_browser.ui.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.domain.user.GetCurrentUserUseCase
import com.ivzb.github_browser.domain.user.GetUserUseCase
import com.ivzb.github_browser.model.ui.User
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val error = MutableLiveData<Event<String>>()
    val user: LiveData<User?>
    val userProfileEvent: MutableLiveData<Event<Pair<UserProfileEvent, String>>> = MutableLiveData()

    private val getUserResult = MutableLiveData<Result<User?>>()

    init {
        user = getUserResult.map {
            loading.postValue(Event(false))

            it.successOr(null).also { user ->
                if (user == null) {
                    error.postValue(Event(COULD_NOT_GET_USER))
                }
            }
        }
    }

    fun getUser(user: String?) {
        loading.postValue(Event(true))

        when (user) {
            null -> getCurrentUserUseCase(Unit, getUserResult)
            else -> getUserUseCase(user, getUserResult)
        }
    }

    fun repositoriesClick(user: String) =
        emitEvent(UserProfileEvent.Repositories, user)

    fun starredClick(user: String) =
        emitEvent(UserProfileEvent.Starred, user)

    fun followingClick(user: String) =
        emitEvent(UserProfileEvent.Following, user)

    fun followersClick(user: String) =
        emitEvent(UserProfileEvent.Followers, user)

    private fun emitEvent(type: UserProfileEvent, user: String) =
        userProfileEvent.postValue(Event(Pair(type, user)))

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
