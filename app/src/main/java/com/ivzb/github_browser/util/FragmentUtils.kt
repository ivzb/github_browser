package com.ivzb.github_browser.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> FragmentActivity.provideViewModel(
    viewModelFactory: ViewModelProvider.Factory
): VM = ViewModelProvider(this, viewModelFactory).get(VM::class.java)

inline fun <reified VM : ViewModel> Fragment.provideViewModel(
    viewModelFactory: ViewModelProvider.Factory
): VM = ViewModelProvider(requireActivity(), viewModelFactory).get(VM::class.java)

fun Fragment.updateTitle(title: String) {
    requireActivity().title = title
}

fun Fragment.updateTitle(@StringRes title: Int) {
    updateTitle(getString(title))
}
