package com.ivzb.github_browser.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivzb.github_browser.domain.login.AccessTokenParameters
import com.ivzb.github_browser.model.access_token.AccessToken
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.ui.Empty
import com.ivzb.github_browser.ui.NoConnection

object TestData {

    val accessTokenParameters = AccessTokenParameters(
        clientId = "d03c9206aecf87f7478f",
        clientSecret = "8ef0eb5b36e4f07c14c206afa87c1f6dbf17a100",
        code = "0a2193871a209292a3ed"
    )

    val accessToken = AccessToken(
        accessToken = "c59898d66264f5f9ed7e1656fb6bbd211d3a82e3",
        tokenType = "bearer"
    )

    val user = User(
        id = 7501124,
        login = "ivzb",
        name = "Ivan Zahariev",
        avatarUrl = "https://avatars1.githubusercontent.com/u/7501124?v=4",
        repos = 41,
        followers = 12,
        following = 79,
        contributions = 5
    )

    val users = listOf(user)

    val repo = Repo(
        id = 262550983,
        name = "retrofit",
        fullName = "square/retrofit",
        description = "",
        owner = "square",
        isFork = true,
        starsCount = 20,
        forksCount = 5,
        watchersCount = 13,
        language = "Java"
    )

    val repos = listOf(repo)

    val noConnection = listOf(NoConnection)

    val empty = listOf(Empty)

    val search = "Retrofit"

    fun <T> liveDataOf(data: T) = MutableLiveData<T>().apply {
        postValue(data)
    }
}