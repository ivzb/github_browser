package com.ivzb.github_browser.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.login.AccessTokenParameters
import com.ivzb.github_browser.domain.login.GetAccessTokenUseCase
import com.ivzb.github_browser.domain.login.SaveAccessTokenUseCase
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.model.access_token.AccessToken
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val loginClick = MutableLiveData<Event<Boolean>>()
    val accessToken: LiveData<AccessToken?>
    val errorMessage = MutableLiveData<Event<String>>()

    private val getAccessTokenResult = MutableLiveData<Result<AccessToken?>>()

    init {
        accessToken = getAccessTokenResult.map {
            loading.postValue(Event(false))

            it.successOr(null).also { accessToken ->
                when (accessToken) {
                    null -> errorMessage.postValue(Event("Couldn't login. Please try again."))
                    else -> saveAccessTokenUseCase(accessToken)
                }
            }
        }
    }

    fun doLogin() {
        loginClick.postValue(Event(true))
    }

    fun getAccessToken(clientId: String, clientSecret: String, code: String?) =
        when (code) {
            null -> errorMessage.postValue(Event(COULD_NOT_LOGIN))

            else -> {
                loading.postValue(Event(true))
                val parameters = AccessTokenParameters(clientId, clientSecret, code)
                getAccessTokenUseCase(parameters, getAccessTokenResult)
            }
        }

    companion object {

        const val COULD_NOT_LOGIN = "Couldn't login. Please try again."
    }
}