package com.ivzb.github_browser.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.login.HasAccessTokenUseCase
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class LauncherViewModel @Inject constructor(
    hasAccessTokenUseCase: HasAccessTokenUseCase
): ViewModel() {

    private val hasAccessTokenResult = MutableLiveData<Result<Boolean>>()
    val launchDestination: LiveData<Event<LaunchDestination>>

    init {
        // Check if access token has already been stored and then navigate the user accordingly
        hasAccessTokenUseCase(Unit, hasAccessTokenResult)
        launchDestination = hasAccessTokenResult.map {
            if ((it as? Result.Success)?.data == false) {
                Event(LaunchDestination.LOGIN)
            } else {
                Event(LaunchDestination.MAIN)
            }
        }
    }
}

enum class LaunchDestination {
    LOGIN,
    MAIN
}
