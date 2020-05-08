package com.ivzb.github_browser.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.login.AccessTokenParameters
import com.ivzb.github_browser.domain.login.GetAccessTokenUseCase
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.model.ui.AccessToken
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val loginClick = MutableLiveData<Event<Boolean>>()
    val accessToken: LiveData<Event<AccessToken?>>

    private val getAccessTokenResult = MutableLiveData<Result<AccessToken?>>()

    init {
        accessToken = getAccessTokenResult.map {
            loading.postValue(Event(false))

            Event(it.successOr(null))
        }
    }

    fun onLogin() {
        loading.postValue(Event(true))
        loginClick.postValue(Event(true))
    }

    fun getAccessToken(clientId: String, clientSecret: String, code: String) {
        val parameters = AccessTokenParameters(clientId, clientSecret, code)
        getAccessTokenUseCase(parameters, getAccessTokenResult)
    }
}