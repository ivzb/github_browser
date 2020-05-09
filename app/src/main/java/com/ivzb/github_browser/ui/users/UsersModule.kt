package com.ivzb.github_browser.ui.users

import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class UsersModule {

    /**
     * Generates an [AndroidInjector] for the [UsersFragment] as a Dagger subcomponent of the
     * [MainModule].
     */
    @ContributesAndroidInjector
    internal abstract fun provideUsersFragment(): UsersFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [UsersViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    abstract fun bindUserProfileFragmentViewModel(viewModel: UsersViewModel): ViewModel
}
