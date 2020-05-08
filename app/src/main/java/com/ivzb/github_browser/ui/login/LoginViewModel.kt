package com.ivzb.github_browser.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    val loginClick = MutableLiveData<Event<Boolean>>()

    fun onLogin() {
        loginClick.postValue(Event(true))
    }
}