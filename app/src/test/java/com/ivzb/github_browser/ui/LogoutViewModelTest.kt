package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.domain.login.ClearAccessTokenUseCase
import com.ivzb.github_browser.ui.logout.LogoutViewModel
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [LogoutViewModel].
 */
class LogoutViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun doLogout_clearsAccessTokenAndEmitsEvent() {
        // Given a ViewModel
        val prefs = mock<PreferenceStorage>()
        val clearAccessTokenUseCase = ClearAccessTokenUseCase(prefs)

        val viewModel = LogoutViewModel(clearAccessTokenUseCase)

        // When login is clicked
        viewModel.doLogout()

        // Then logout event should be emitted and access token clearer
        val logoutEvent = LiveDataTestUtil.getValue(viewModel.logout)
        assertThat(logoutEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        verify(prefs).accessToken = null
    }
}
