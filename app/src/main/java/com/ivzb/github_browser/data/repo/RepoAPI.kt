package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.network.RepoResponse
import retrofit2.Call
import retrofit2.http.*

interface RepoAPI {

    @Headers("Accept: application/json")
    @GET("users/{user}/repos")
    fun getRepo(@Path("user") user: String): Call<List<RepoResponse>>
}
