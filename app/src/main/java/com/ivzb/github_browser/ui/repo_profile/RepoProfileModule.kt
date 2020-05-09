package com.ivzb.github_browser.ui.repo_profile

import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class RepoProfileModule {

    /**
     * Generates an [AndroidInjector] for the [RepoProfileFragment] as a Dagger subcomponent of the
     * [MainModule].
     */
    @ContributesAndroidInjector
    internal abstract fun provideRepoProfileFragment(): RepoProfileFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [RepoProfileViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(RepoProfileViewModel::class)
    abstract fun bindRepoProfileFragmentViewModel(viewModel: RepoProfileViewModel): ViewModel
}
