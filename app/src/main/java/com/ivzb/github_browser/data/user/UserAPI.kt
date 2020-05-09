package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.network.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {

    @Headers("Accept: application/json")
    @GET("user")
    fun getCurrentUser(): Call<UserResponse>
}
