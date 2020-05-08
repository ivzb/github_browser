package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.login.LoginRepository
import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.domain.login.GetAccessTokenUseCase
import com.ivzb.github_browser.domain.login.SaveAccessTokenUseCase
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.ui.login.LoginViewModel
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [LoginViewModel].
 */
class LoginViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun loginClicked_triggersLoadingAndClickEvent() {
        // Given a ViewModel
        val loginRepository = mock<LoginRepository>()
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When login is clicked
        viewModel.onLogin()

        // Then loading and click event should be triggered
        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loginClick)
        assertThat(loadingEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val loginClickEvent = LiveDataTestUtil.getValue(viewModel.loginClick)
        assertThat(loginClickEvent?.peekContent(), `is`(CoreMatchers.equalTo(true)))
    }

    @Test
    fun getAccessToken_succeedsAndUpdatesPrefs() {
        // Given a ViewModel
        val parameters = TestData.accessTokenParameters

        val loginRepository = mock<LoginRepository> {
            on {
                getAccessToken(
                    eq(parameters.clientId),
                    eq(parameters.clientSecret),
                    eq(parameters.code)
                )
            }.thenReturn(TestData.accessToken)
        }
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When access token is requested
        viewModel.getAccessToken(parameters)

        // Check that data was loaded correctly and prefs were updated
        val accessToken = LiveDataTestUtil.getValue(viewModel.accessToken)
        assertThat(accessToken, `is`(CoreMatchers.equalTo(TestData.accessToken)))

        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        verify(prefs).accessToken = TestData.accessToken.accessToken
        verify(prefs).tokenType = TestData.accessToken.tokenType
    }

    @Test
    fun getAccessToken_failsAndSendsErrorMessage() {
        // Given a ViewModel
        val parameters = TestData.accessTokenParameters

        val loginRepository = mock<LoginRepository> {
            on {
                getAccessToken(
                    eq(parameters.clientId),
                    eq(parameters.clientSecret),
                    eq(parameters.code)
                )
            }.thenReturn(null)
        }
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When access token is requested and fails
        viewModel.getAccessToken(parameters)

        // Check that data was loaded correctly and error was sent
        val accessToken = LiveDataTestUtil.getValue(viewModel.accessToken)
        assertTrue(accessToken == null)

        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        val errorMessage = LiveDataTestUtil.getValue(viewModel.errorMessage)
        assertThat(errorMessage?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo("Couldn't login. Please try again.")))

        verifyZeroInteractions(prefs)
    }
}