package com.ivzb.github_browser.ui.main

import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}