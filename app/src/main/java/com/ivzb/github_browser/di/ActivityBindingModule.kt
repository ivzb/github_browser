package com.ivzb.github_browser.di

import com.ivzb.github_browser.ui.launcher.LauncherActivity
import com.ivzb.github_browser.ui.launcher.LauncherModule
import com.ivzb.github_browser.ui.login.LoginActivity
import com.ivzb.github_browser.ui.login.LoginModule
import com.ivzb.github_browser.ui.logout.LogoutModule
import com.ivzb.github_browser.ui.main.MainActivity
import com.ivzb.github_browser.ui.main.MainModule
import com.ivzb.github_browser.ui.repo_profile.RepoProfileModule
import com.ivzb.github_browser.ui.repos.ReposModule
import com.ivzb.github_browser.ui.search.SearchModule
import com.ivzb.github_browser.ui.user_profile.UserProfileModule
import com.ivzb.github_browser.ui.users.UsersModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LauncherModule::class])
    internal abstract fun launcherActivity(): LauncherActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            MainModule::class,
            UserProfileModule::class,
            UsersModule::class,
            RepoProfileModule::class,
            ReposModule::class,
            SearchModule::class,
            LogoutModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}