package com.ivzb.github_browser.domain.login

import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.domain.UseCase
import javax.inject.Inject

class ClearAccessTokenUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, Boolean>() {

    override fun execute(parameters: Unit): Boolean {
        preferenceStorage.accessToken = null
        return true
    }
}
