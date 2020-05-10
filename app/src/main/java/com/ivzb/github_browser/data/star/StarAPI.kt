package com.ivzb.github_browser.data.star

import retrofit2.Call
import retrofit2.http.*

interface StarAPI {

    @GET("user/starred/{repo}")
    fun isStarred(@Path("repo", encoded = true) repo: String): Call<Unit>

    @PUT("user/starred/{repo}")
    fun star(@Path("repo", encoded = true) repo: String): Call<Unit>

    @DELETE("user/starred/{repo}")
    fun unstar(@Path("repo", encoded = true) repo: String): Call<Unit>
}
