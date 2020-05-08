package com.ivzb.github_browser.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.BuildConfig
import com.ivzb.github_browser.R
import com.ivzb.github_browser.databinding.ActivityLoginBinding
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.domain.login.AccessTokenParameters
import com.ivzb.github_browser.ui.main.MainActivity
import com.ivzb.github_browser.util.provideViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = provideViewModel(viewModelFactory)

        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            viewModel = loginViewModel
            lifecycleOwner = this@LoginActivity
        }

        loginViewModel.loginClick.observe(this, EventObserver {
            openGithubAuthorization()
        })

        loginViewModel.accessToken.observe(this, Observer {
            it?.let {
                openMainScreen()
            }
        })

        loginViewModel.errorMessage.observe(this, Observer {
            showErrorMessage(it.peekContent())
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.getQueryParameter(CODE)?.let { code ->
            loginViewModel.getAccessToken(
                AccessTokenParameters(BuildConfig.client_id, BuildConfig.client_secret, code)
            )
        }
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

    private fun openMainScreen() {
        MainActivity.start(this)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        private const val CLIENT_ID = "client_id"
        private const val REDIRECT_URI = "redirect_uri"
        private const val SCOPE = "scope"
        private const val REPO = "repo"
        private const val CODE = "code"

        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}