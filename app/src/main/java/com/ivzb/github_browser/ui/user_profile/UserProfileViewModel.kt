package com.ivzb.github_browser.ui.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.domain.user.GetCurrentUserUseCase
import com.ivzb.github_browser.model.ui.User
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val errorMessage = MutableLiveData<Event<String>>()
    val user: LiveData<User?>

    private val getCurrentUserResult = MutableLiveData<Result<User?>>()

    init {
        user = getCurrentUserResult.map {
            loading.postValue(Event(false))

            val user = it.successOr(null)

            if (user == null) {
                errorMessage.postValue(Event("Couldn't get user. Please try again."))
            }

            user
        }
    }

    fun getUser(user: String?) {
        when (user) {
            null -> {
                getCurrentUserUseCase(Unit, getCurrentUserResult)
            }

            else -> {

            }
        }
    }
}
