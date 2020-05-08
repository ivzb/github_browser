package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.domain.login.HasAccessTokenUseCase
import com.ivzb.github_browser.ui.launcher.LaunchDestination
import com.ivzb.github_browser.ui.launcher.LauncherViewModel
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [LauncherViewModel].
 */
class LauncherViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun notObtainedAccessToken_navigatesToLogin() {
        // Given that user has *not* obtained access token
        val prefs = mock<PreferenceStorage>()
        val hasAccessTokenUseCase = HasAccessTokenUseCase(prefs)
        val viewModel = LauncherViewModel(hasAccessTokenUseCase)

        // When launchDestination is observed
        // Then verify user is navigated to the login activity
        val navigateEvent = LiveDataTestUtil.getValue(viewModel.launchDestination)
        assertEquals(LaunchDestination.LOGIN, navigateEvent?.getContentIfNotHandled())
    }

    @Test
    fun hasAccessToken_navigatesToMainActivity() {
        // Given that user *has* obtained access token
        val prefs = mock<PreferenceStorage> {
            on { accessToken }.doReturn("random access token")
        }
        val hasAccessTokenUseCase = HasAccessTokenUseCase(prefs)
        val viewModel = LauncherViewModel(hasAccessTokenUseCase)

        // When launchDestination is observed
        // Then verify user is navigated to the main activity
        val navigateEvent = LiveDataTestUtil.getValue(viewModel.launchDestination)
        assertEquals(LaunchDestination.MAIN, navigateEvent?.getContentIfNotHandled())
    }
}
