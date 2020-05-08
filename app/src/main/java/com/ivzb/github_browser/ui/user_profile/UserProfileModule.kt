package com.ivzb.github_browser.ui.user_profile

import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class UserProfileModule {

    /**
     * Generates an [AndroidInjector] for the [UserProfileFragment] as a Dagger subcomponent of the
     * [MainModule].
     */
    @ContributesAndroidInjector
    internal abstract fun provideUserProfileFragment(): UserProfileFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [UserProfileViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    abstract fun bindUserProfileFragmentViewModel(viewModel: UserProfileViewModel): ViewModel
}
