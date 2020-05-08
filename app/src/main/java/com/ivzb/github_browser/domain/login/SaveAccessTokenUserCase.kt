package com.ivzb.github_browser.domain.login

import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.ui.AccessToken
import javax.inject.Inject

class SaveAccessTokenUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<AccessToken, Unit>() {

    override fun execute(parameters: AccessToken) {
        preferenceStorage.accessToken = parameters.accessToken
        preferenceStorage.tokenType = parameters.tokenType
    }
}
