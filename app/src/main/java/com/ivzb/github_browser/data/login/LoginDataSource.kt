package com.ivzb.github_browser.data.login

import com.ivzb.github_browser.model.access_token.AccessToken

interface LoginDataSource {

    fun getAccessToken(clientId: String, clientSecret: String, code: String): AccessToken?
}