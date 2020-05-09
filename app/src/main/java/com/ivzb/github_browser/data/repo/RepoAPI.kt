package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.network.RepoResponse
import retrofit2.Call
import retrofit2.http.*

interface RepoAPI {

    @Headers("Accept: application/json")
    @GET("users/{user}/repos")
    fun getOwnRepos(@Path("user") user: String): Call<List<RepoResponse>>

    @Headers("Accept: application/json")
    @GET("users/{user}/starred")
    fun getStarredRepos(@Path("user") user: String): Call<List<RepoResponse>>

    @Headers("Accept: application/json")
    @GET("repos/{repo}")
    fun getRepo(@Path("repo", encoded = true) repo: String): Call<RepoResponse>
}
