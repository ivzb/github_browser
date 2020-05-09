package com.ivzb.github_browser.di

import com.ivzb.github_browser.BuildConfig
import com.ivzb.github_browser.data.AuthorizationInterceptor
import com.ivzb.github_browser.data.login.LoginDataSource
import com.ivzb.github_browser.data.login.RemoteLoginDataSource
import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.data.repo.RemoteRepoDataSource
import com.ivzb.github_browser.data.repo.RepoDataSource
import com.ivzb.github_browser.data.user.RemoteUserDataSource
import com.ivzb.github_browser.data.user.UserDataSource
import com.ivzb.github_browser.util.NetworkUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideOkHttp(preferenceStorage: PreferenceStorage): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(preferenceStorage))

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.api_url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(
        networkUtils: NetworkUtils,
        retrofit: Retrofit
    ): LoginDataSource = RemoteLoginDataSource(networkUtils, retrofit)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        networkUtils: NetworkUtils,
        retrofit: Retrofit
    ): UserDataSource = RemoteUserDataSource(networkUtils, retrofit)

    @Singleton
    @Provides
    fun provideRepoRemoteDataSource(
        networkUtils: NetworkUtils,
        retrofit: Retrofit
    ): RepoDataSource = RemoteRepoDataSource(networkUtils, retrofit)
}
