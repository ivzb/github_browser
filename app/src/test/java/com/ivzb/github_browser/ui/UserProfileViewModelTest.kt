package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.user.FetchUserUseCase
import com.ivzb.github_browser.domain.user.ObserveUserUseCase
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
            on { observeUser(eq(null)) }.thenReturn(TestData.liveDataOf(TestData.user))
            on { fetchUser(eq(null)) }.thenReturn(true)
        }
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When current user is requested
        viewModel.getUser(null)

        // Then event should be emitted
        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertThat(user?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.user)))

        verify(userRepository).fetchUser(eq(null))

        val loading = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loading?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getCurrentUser_fails() {
        // Given a ViewModel
        val userRepository = mock<UserRepository> {
            on { observeUser(eq(null)) }.thenReturn(TestData.liveDataOf(null))
            on { fetchUser(eq(null)) }.thenReturn(false)
        }
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When current user is requested
        viewModel.getUser(null)

        // Then loading and error events should be emitted
        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertTrue(user?.getContentIfNotHandled() == null)

        verify(userRepository).fetchUser(eq(null))

        val loading = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loading?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        val errorEvent = LiveDataTestUtil.getValue(viewModel.error)
        assertThat(
            errorEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(COULD_NOT_GET_USER))
        )
    }

    @Test
    fun getUser_succeeds() {
        // Given a ViewModel
        val userName = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { observeUser(eq(userName)) }.thenReturn(TestData.liveDataOf(TestData.user))
            on { fetchUser(eq(userName)) }.thenReturn(true)
        }
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When current user is requested
        viewModel.getUser(userName)

        // Then event should be emitted
        verify(userRepository).fetchUser(eq(userName))

        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertThat(user?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.user)))

        val loading = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loading?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getUser_fails() {
        // Given a ViewModel
        val userName = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { observeUser(eq(userName)) }.thenReturn(TestData.liveDataOf(null))
            on { fetchUser(eq(userName)) }.thenReturn(false)
        }
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When current user is requested
        viewModel.getUser(userName)

        // Then loading and error events should be emitted
        val user = LiveDataTestUtil.getValue(viewModel.user)
        assertTrue(user?.getContentIfNotHandled() == null)

        verify(userRepository).fetchUser(eq(userName))

        val loading = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loading?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        val errorEvent = LiveDataTestUtil.getValue(viewModel.error)
        assertThat(
            errorEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(COULD_NOT_GET_USER))
        )
    }

    @Test
    fun ownReposClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When repos is clicked
        viewModel.ownReposClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(
            userProfileEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Repositories, TestData.user.login)))
        )
    }

    @Test
    fun starredReposClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When repos is clicked
        viewModel.starredReposClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(
            userProfileEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Starred, TestData.user.login)))
        )
    }

    @Test
    fun followingClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When following is clicked
        viewModel.followingClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(
            userProfileEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Following, TestData.user.login)))
        )
    }

    @Test
    fun followersClick_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUserUseCase = ObserveUserUseCase(userRepository)
        val fetchUserUseCase = FetchUserUseCase(userRepository)

        val viewModel = UserProfileViewModel(observeUserUseCase, fetchUserUseCase)

        // When followers is clicked
        viewModel.followersClick(TestData.user.login)

        // Then event should be emitted
        val userProfileEvent = LiveDataTestUtil.getValue(viewModel.userProfileEvent)
        assertThat(
            userProfileEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(Pair(UserProfileEvent.Followers, TestData.user.login)))
        )
    }
}
