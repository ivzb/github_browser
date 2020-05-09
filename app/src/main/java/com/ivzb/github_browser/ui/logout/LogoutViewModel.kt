package com.ivzb.github_browser.ui.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.login.ClearAccessTokenUseCase
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase
): ViewModel() {

    val logout: LiveData<Event<Boolean>>

    private val clearAccessTokenResult = MutableLiveData<Result<Boolean>>()

    init {
        logout = clearAccessTokenResult.map {
            Event(it.successOr(true))
        }
    }

    fun doLogout() {
        clearAccessTokenUseCase(Unit, clearAccessTokenResult)
    }
}
