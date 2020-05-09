package com.ivzb.github_browser.ui.logout

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.databinding.FragmentLogoutBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.ui.login.LoginActivity
import com.ivzb.github_browser.util.provideViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LogoutFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val searchViewModel: LogoutViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentLogoutBinding = FragmentLogoutBinding.inflate(inflater, container, false).apply {
            viewModel = searchViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        searchViewModel.logout.observe(viewLifecycleOwner, EventObserver {
            openLogin()
        })

        searchViewModel.doLogout()

        return binding.root
    }

    private fun openLogin() = requireActivity().run {
        LoginActivity.start(this)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
