package com.ivzb.github_browser.data

import com.ivzb.github_browser.model.network.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.*

interface GithubAPI {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Call<AccessTokenResponse>
}