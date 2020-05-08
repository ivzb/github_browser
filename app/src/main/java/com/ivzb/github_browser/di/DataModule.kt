package com.ivzb.github_browser.di

import com.ivzb.github_browser.BuildConfig
import com.ivzb.github_browser.data.login.LoginDataSource
import com.ivzb.github_browser.data.login.RemoteLoginDataSource
import com.ivzb.github_browser.util.NetworkUtils
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(
        networkUtils: NetworkUtils,
        retrofit: Retrofit
    ): LoginDataSource = RemoteLoginDataSource(networkUtils, retrofit)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}