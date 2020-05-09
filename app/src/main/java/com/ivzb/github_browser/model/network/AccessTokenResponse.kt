package com.ivzb.github_browser.model.network

import com.google.gson.annotations.SerializedName
import com.ivzb.github_browser.model.ui.AccessToken

data class AccessTokenResponse(

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String
) {

    fun asAccessToken() = AccessToken(
        accessToken = this.accessToken,
        tokenType = this.tokenType
    )
}