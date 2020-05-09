package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.user.GetCurrentUserUseCase
import com.ivzb.github_browser.domain.user.GetUserUseCase
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.ui.user_profile.UserProfileEvent
import com.ivzb.github_browser.ui.user_profile.UserProfileViewModel
import com.ivzb.github_browser.ui.user_profile.UserProfileViewModel.Companion.COULD_NOT_GET_USER
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
 * Unit tests for the [UserProfileViewModel].
 */
class UserProfileViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun getCurrentUser_succeeds() {
        // Given a ViewModel
        val userRepository = mock<UserRepository> {
            on { getCurrentUser() }.thenReturn(TestData.user)
        }
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When current user is requested
        viewModel.getUser(null)

        // Then event should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertThat(user, `is`(CoreMatchers.equalTo(TestData.user)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getCurrentUser_fails() {
        // Given a ViewModel
        val userRepository = mock<UserRepository> {
            on { getCurrentUser() }.thenReturn(null)
        }
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When current user is requested
        viewModel.getUser(null)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertTrue(user == null)

        val errorEvent = LiveDataTestUtil.getValue(viewModel.error)
        assertThat(errorEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(COULD_NOT_GET_USER)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getUser_succeeds() {
        // Given a ViewModel
        val userName = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getUser(eq(userName)) }.thenReturn(TestData.user)
        }
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When current user is requested
        viewModel.getUser(userName)

        // Then event should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertThat(user, `is`(CoreMatchers.equalTo(TestData.user)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getUser_fails() {
        // Given a ViewModel
        val userName = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getUser(eq(userName)) }.thenReturn(null)
        }
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When current user is requested
        viewModel.getUser(userName)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertTrue(user == null)

        val errorEvent = LiveDataTestUtil.getValue(viewModel.error)
        assertThat(errorEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(COULD_NOT_GET_USER)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun repositoriesClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When login is clicked
        viewModel.repositoriesClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(userProfileEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Repositories, TestData.user.login))))
    }

    @Test
    fun starsClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When login is clicked
        viewModel.starsClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(userProfileEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Stars, TestData.user.login))))
    }

    @Test
    fun followingClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When login is clicked
        viewModel.followingClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(userProfileEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Following, TestData.user.login))))
    }

    @Test
    fun followersClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(getCurrentUserUseCase, getUserUseCase)

        // When login is clicked
        viewModel.followersClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(userProfileEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Followers, TestData.user.login))))
    }
}
