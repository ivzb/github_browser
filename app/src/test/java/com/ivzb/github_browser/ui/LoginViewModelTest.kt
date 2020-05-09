package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.login.LoginRepository
import com.ivzb.github_browser.data.preference.PreferenceStorage
import com.ivzb.github_browser.domain.login.GetAccessTokenUseCase
import com.ivzb.github_browser.domain.login.SaveAccessTokenUseCase
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.ui.login.LoginViewModel
import com.ivzb.github_browser.ui.login.LoginViewModel.Companion.COULD_NOT_LOGIN
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
    fun doLogin_triggersLoadingAndClickEvent() {
        // Given a ViewModel
        val loginRepository = mock<LoginRepository>()
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When login is invoked
        viewModel.doLogin()

        // Then loading and login event should be emitted
        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loginClick)
        assertThat(loadingEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))
    }

    @Test
    fun getAccessToken_succeedsAndUpdatesPrefs() {
        // Given a ViewModel
        val (clientId, clientSecret, code) = TestData.accessTokenParameters

        val loginRepository = mock<LoginRepository> {
            on {
                getAccessToken(
                    eq(clientId),
                    eq(clientSecret),
                    eq(code)
                )
            }.thenReturn(TestData.accessToken)
        }
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When access token is requested
        viewModel.getAccessToken(clientId, clientSecret, code)

        // Check that data was loaded correctly and prefs were updated
        val accessToken = LiveDataTestUtil.getValue(viewModel.accessToken)
        assertThat(accessToken, `is`(CoreMatchers.equalTo(TestData.accessToken)))

        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        verify(prefs).accessToken = "${TestData.accessToken.tokenType} ${TestData.accessToken.accessToken}"
    }

    @Test
    fun getAccessToken_failsAndSendsErrorMessage() {
        // Given a ViewModel
        val (clientId, clientSecret, code) = TestData.accessTokenParameters

        val loginRepository = mock<LoginRepository> {
            on {
                getAccessToken(
                    eq(clientId),
                    eq(clientSecret),
                    eq(code)
                )
            }.thenReturn(null)
        }
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When access token is requested and fails
        viewModel.getAccessToken(clientId, clientSecret, code)

        // Check that data was loaded correctly and error was sent
        val accessToken = LiveDataTestUtil.getValue(viewModel.accessToken)
        assertTrue(accessToken == null)

        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        val errorMessage = LiveDataTestUtil.getValue(viewModel.errorMessage)
        assertThat(errorMessage?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(COULD_NOT_LOGIN)))

        verifyZeroInteractions(prefs)
    }

    @Test
    fun getAccessToken_emptyCodeSendsErrorMessage() {
        // Given a ViewModel
        val (clientId, clientSecret, _) = TestData.accessTokenParameters

        val loginRepository = mock<LoginRepository>()
        val getAccessTokenUseCase = GetAccessTokenUseCase(loginRepository)

        val prefs = mock<PreferenceStorage>()
        val saveAccessTokenUseCase = SaveAccessTokenUseCase(prefs)

        val viewModel = LoginViewModel(getAccessTokenUseCase, saveAccessTokenUseCase)

        // When access token is requested with code null
        viewModel.getAccessToken(clientId, clientSecret, null)

        // Check that error was sent
        val accessToken = LiveDataTestUtil.getValue(viewModel.accessToken)
        assertTrue(accessToken == null)

        val loadingEvent = LiveDataTestUtil.getValue(viewModel.loading)
        assertTrue(loadingEvent == null)

        val errorMessage = LiveDataTestUtil.getValue(viewModel.errorMessage)
        assertThat(errorMessage?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(COULD_NOT_LOGIN)))

        verifyZeroInteractions(prefs)
    }
}