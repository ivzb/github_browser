package com.ivzb.github_browser.ui.repos

import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ReposModule {

    /**
     * Generates an [AndroidInjector] for the [ReposFragment] as a Dagger subcomponent of the
     * [MainModule].
     */
    @ContributesAndroidInjector
    internal abstract fun provideReposFragment(): ReposFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [ReposViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    abstract fun bindUserProfileFragmentViewModel(viewModel: ReposViewModel): ViewModel
}
