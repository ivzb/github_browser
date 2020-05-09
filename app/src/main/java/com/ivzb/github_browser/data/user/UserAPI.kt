package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.network.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {

    @Headers("Accept: application/json")
    @GET("user")
    fun getCurrentUser(): Call<UserResponse>

    @Headers("Accept: application/json")
    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Call<UserResponse>

    @Headers("Accept: application/json")
    @GET("users/{user}/following")
    fun getFollowing(@Path("user") user: String): Call<List<UserResponse>>

    @Headers("Accept: application/json")
    @GET("users/{user}/followers")
    fun getFollowers(@Path("user") user: String): Call<List<UserResponse>>

    @Headers("Accept: application/json")
    @GET("repos/{repo}/contributors")
    fun getContributors(@Path("repo", encoded = true) repo: String): Call<List<UserResponse>>
}
