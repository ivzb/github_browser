package com.ivzb.github_browser.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    val code = MutableLiveData<String>()

    fun sendCode(value: String) {
        code.postValue(value)
    }
}
