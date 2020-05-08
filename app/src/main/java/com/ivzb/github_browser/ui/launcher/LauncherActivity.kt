package com.ivzb.github_browser.ui.launcher

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ivzb.github_browser.domain.EventObserver
import com.ivzb.github_browser.ui.login.LoginActivity
import com.ivzb.github_browser.ui.main.MainActivity
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.provideViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * A 'Trampoline' activity for sending users to main screen on launch.
 */
class LauncherActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: LauncherViewModel = provideViewModel(viewModelFactory)
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN -> MainActivity.start(this)
                LaunchDestination.LOGIN -> LoginActivity.start(this)
            }.checkAllMatched

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        })
    }
}