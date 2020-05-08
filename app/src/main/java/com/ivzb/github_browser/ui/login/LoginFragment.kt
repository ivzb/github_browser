package com.ivzb.github_browser.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.BuildConfig
import com.ivzb.github_browser.databinding.FragmentLoginBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.ui.main.MainViewModel
import com.ivzb.github_browser.util.provideViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val loginViewModel: LoginViewModel = provideViewModel(viewModelFactory)
        val mainViewModel: MainViewModel = provideViewModel(viewModelFactory)

        val binding: FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            viewModel = loginViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        loginViewModel.loginClick.observe(viewLifecycleOwner, EventObserver {
            openGithubAuthorization()
        })

        loginViewModel.accessToken.observe(viewLifecycleOwner, Observer {
            openUserScreen()
        })

        mainViewModel.code.observe(viewLifecycleOwner, Observer { code ->
            loginViewModel.getAccessToken(BuildConfig.client_id, BuildConfig.client_secret, code)
        })

        return binding.root
    }

    private fun openGithubAuthorization() {
        val uri = Uri.Builder().run {
            scheme(BuildConfig.authorization_scheme)
            path(BuildConfig.authorization_path)
            appendQueryParameter(CLIENT_ID, BuildConfig.client_id)
            appendQueryParameter(REDIRECT_URI, BuildConfig.redirect_uri)
            appendQueryParameter(SCOPE, REPO)
            build()
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun openUserScreen() {
        Toast.makeText(requireContext(), "open user screen", Toast.LENGTH_LONG).show()
    }

    companion object {

        const val CLIENT_ID = "client_id"
        const val REDIRECT_URI = "redirect_uri"
        const val SCOPE = "scope"
        const val REPO = "repo"
    }
}