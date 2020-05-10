package com.ivzb.github_browser.data.login

import com.ivzb.github_browser.BuildConfig
import com.ivzb.github_browser.model.access_token.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {

    @Headers("Accept: application/json")
    @POST("${BuildConfig.oauth_url}login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Call<AccessTokenResponse>
}