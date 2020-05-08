package com.ivzb.github_browser.ui.login

import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class LoginModule {

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [LoginViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginFragmentViewModel(viewModel: LoginViewModel): ViewModel
}