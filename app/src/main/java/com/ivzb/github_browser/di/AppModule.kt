package com.ivzb.github_browser.di

import android.content.Context
import com.ivzb.github_browser.MainApplication
import dagger.Module
import dagger.Provides

/**
 * Define all the classes that need to be provided in the scope of the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@Module
class AppModule {

    @Provides
    fun provideContext(application: MainApplication): Context = application.applicationContext
}